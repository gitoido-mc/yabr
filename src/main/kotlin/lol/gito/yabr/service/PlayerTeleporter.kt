package lol.gito.yabr.service

import net.minecraft.core.BlockPos
import net.minecraft.core.BlockPos.MutableBlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.BlockTags
import net.minecraft.tags.FluidTags
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Relative
import net.minecraft.world.level.BlockGetter
import kotlin.collections.setOf
import kotlin.math.min
import kotlin.math.sqrt

object PlayerTeleporter {
    fun teleportPlayer(
        level: ServerLevel,
        player: ServerPlayer,
        pos: BlockPos,
    ) {
        player.teleportTo(
            level,
            pos.x.toDouble(),
            pos.y.toDouble(),
            pos.z.toDouble(),
            setOf<Relative>(),
            0.0f,
            0.0f,
            true,
        )
    }

    fun spreadPlayers(
        level: ServerLevel,
        players: List<ServerPlayer>,
        maxHeight: Int = 256,
    ) {
        if (players.isEmpty()) return

        val random = RandomSource.createThreadLocalInstance()
        val border = level.worldBorder
        val minX = border.minX + 1.0
        val minZ = border.minZ + 1.0
        val maxX = border.maxX - 1.0
        val maxZ = border.maxZ - 1.0
        val spreadDistance =
            min(
                256.0,
                min(maxX - minX, maxZ - minZ) / sqrt(players.size.toDouble()),
            )
        val positions =
            createInitialPositions(
                random,
                players.count(),
                minX,
                minZ,
                maxX,
                maxZ,
            )
        spreadPositions(
            spreadDistance,
            level,
            random,
            minX,
            minZ,
            maxX,
            maxZ,
            maxHeight,
            positions,
        )

        setPlayerPositions(
            players,
            level,
            positions,
            maxHeight,
        )
    }

    private fun createInitialPositions(
        random: RandomSource,
        count: Int,
        minX: Double,
        minZ: Double,
        maxX: Double,
        maxZ: Double,
    ): List<Position> {
        val result = mutableListOf<Position>()

        repeat(count) {
            result.add(Position().apply { this.randomize(random, minX, minZ, maxX, maxZ) })
        }

        return result
    }

    private fun spreadPositions(
        spreadDist: Double,
        level: ServerLevel,
        random: RandomSource,
        minX: Double,
        minZ: Double,
        maxX: Double,
        maxZ: Double,
        maxHeight: Int,
        positions: List<Position>,
    ) {
        var hasCollisions = true
        var iteration = 0
        while (iteration < 10000 && hasCollisions) {
            hasCollisions = false

            for (i in positions.indices) {
                val position = positions[i]
                var neighbourCount = 0
                val averageNeighbourPos = Position()

                for (j in positions.indices) {
                    if (i != j) {
                        val neighbour = positions[j]
                        val dist = position.dist(neighbour)
                        if (dist < spreadDist) {
                            neighbourCount++
                            averageNeighbourPos.x += (neighbour.x - position.x)
                            averageNeighbourPos.z += (neighbour.z - position.z)
                        }
                    }
                }

                if (neighbourCount > 0) {
                    averageNeighbourPos.x /= neighbourCount.toDouble()
                    averageNeighbourPos.z /= neighbourCount.toDouble()
                    val length = averageNeighbourPos.length
                    if (length > 0.0) {
                        averageNeighbourPos.normalize()
                        position.moveAway(averageNeighbourPos)
                    } else {
                        position.randomize(random, minX, minZ, maxX, maxZ)
                    }

                    hasCollisions = true
                }

                if (position.clamp(minX, minZ, maxX, maxZ)) {
                    hasCollisions = true
                }
            }

            if (!hasCollisions) {
                for (position in positions) {
                    if (!position.isSafe(level, maxHeight)) {
                        position.randomize(random, minX, minZ, maxX, maxZ)
                        hasCollisions = true
                    }
                }
            }
            iteration++
        }
    }

    private fun setPlayerPositions(
        entities: List<Entity>,
        level: ServerLevel,
        positions: List<Position>,
        maxHeight: Int,
    ): Double {
        var avgDistance = 0.0
        var positionIndex = 0

        for (entity in entities) {
            val position: Position = positions[positionIndex++]

            entity.teleportTo(
                level,
                Mth.floor(position.x) + 0.5,
                position.getSpawnY(level, maxHeight).toDouble(),
                Mth.floor(position.z) + 0.5,
                setOf(),
                entity.yRot,
                entity.xRot,
                true,
            )

            var closest = Double.MAX_VALUE

            positions.forEach {
                if (position != it) closest = min(position.dist(it), closest)
            }

            avgDistance += closest
        }

        return if (entities.size < 2) 0.0 else avgDistance / entities.size
    }

    private class Position {
        var x = 0.0
        var z = 0.0

        override fun equals(other: Any?): Boolean {
            if (other is Position) {
                return this.x == other.x && this.z == other.z
            }
            return super.equals(other)
        }

        fun dist(target: Position): Double {
            val dx = this.x - target.x
            val dz = this.z - target.z
            return sqrt(dx * dx + dz * dz)
        }

        fun normalize() {
            val dist = this.length
            this.x /= dist
            this.z /= dist
        }

        val length: Double
            get() = sqrt(this.x * this.x + this.z * this.z)

        fun moveAway(pos: Position) {
            this.x -= pos.x
            this.z -= pos.z
        }

        fun clamp(
            minX: Double,
            minZ: Double,
            maxX: Double,
            maxZ: Double,
        ): Boolean {
            var changed = false
            if (this.x < minX) {
                this.x = minX
                changed = true
            } else if (this.x > maxX) {
                this.x = maxX
                changed = true
            }

            if (this.z < minZ) {
                this.z = minZ
                changed = true
            } else if (this.z > maxZ) {
                this.z = maxZ
                changed = true
            }

            return changed
        }

        fun getSpawnY(
            level: BlockGetter,
            maxHeight: Int,
        ): Int {
            val pos = MutableBlockPos(this.x, (maxHeight + 1).toDouble(), this.z)
            var air2Above = level.getBlockState(pos).isAir
            pos.move(Direction.DOWN)
            var air1Above = level.getBlockState(pos).isAir

            while (pos.y > level.minY) {
                pos.move(Direction.DOWN)
                val currentIsAir = level.getBlockState(pos).isAir
                if (!currentIsAir && air1Above && air2Above) {
                    return pos.y + 1
                }

                air2Above = air1Above
                air1Above = currentIsAir
            }

            return maxHeight + 1
        }

        fun isSafe(
            level: BlockGetter,
            maxHeight: Int,
        ): Boolean {
            val pos = BlockPos.containing(this.x, (this.getSpawnY(level, maxHeight) - 1).toDouble(), this.z)
            val state = level.getBlockState(pos)
            return pos.y < maxHeight &&
                !state.fluidState.`is`(FluidTags.LAVA) &&
                !state.fluidState.`is`(FluidTags.WATER) &&
                !state.`is`(BlockTags.FIRE)
        }

        fun randomize(
            random: RandomSource,
            minX: Double,
            minZ: Double,
            maxX: Double,
            maxZ: Double,
        ) {
            this.x = Mth.nextDouble(random, minX, maxX)
            this.z = Mth.nextDouble(random, minZ, maxZ)
        }

        override fun hashCode(): Int {
            var result = x.hashCode()
            result = 31 * result + z.hashCode()
            result = 31 * result + length.hashCode()
            return result
        }
    }
}
