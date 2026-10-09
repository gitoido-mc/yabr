package lol.gito.yabr.state

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import lol.gito.yabr.helper.RecordCodecDef
import lol.gito.yabr.registry.StorageRegistry
import net.minecraft.core.BlockPos
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Difficulty
import net.minecraft.world.level.saveddata.SavedData

class GameState(
    center: BlockPos? = null,
    difficulty: Difficulty? = null,
    radius: Int? = null,
    intervalTime: Int? = null,
    roundTime: Int? = null,
    claimedLocations: MutableList<BlockPos> = mutableListOf(),
) : SavedData() {
    var center: BlockPos = BlockPos.ZERO
        set(value) {
            field = value
            setDirty()
        }
    var difficulty: Difficulty = Difficulty.NORMAL
        set(value) {
            field = value
            setDirty()
        }
    var radius: Int = 128
        set(value) {
            field = value
            setDirty()
        }
    var intervalTime: Int = 300
        set(value) {
            field = value
            setDirty()
        }
    var roundTime: Int = 600
        set(value) {
            field = value
            setDirty()
        }
    var claimedLocations: MutableList<BlockPos> = mutableListOf()
        set(value) {
            field = value
            setDirty()
        }

    init {
        if (center != null) this.center = center
        if (difficulty != null) this.difficulty = difficulty
        if (radius != null) this.radius = radius
        if (intervalTime != null) this.intervalTime = intervalTime
        if (roundTime != null) this.roundTime = roundTime
        val newLocations =
            this.claimedLocations.apply {
                addAll(claimedLocations)
            }
        this.claimedLocations = newLocations.distinct().toMutableList()
    }

    fun addClaimedPos(pos: BlockPos) {
        claimedLocations.add(pos.atY(0))
        setDirty()
    }

    companion object {
        private val codecDefinition: RecordCodecDef<GameState> = {
            it
                .group(
                    BlockPos.CODEC.fieldOf("center").forGetter(GameState::center),
                    Difficulty.CODEC.fieldOf("difficulty").forGetter(GameState::difficulty),
                    Codec.INT.fieldOf("radius").forGetter(GameState::radius),
                    Codec.INT.fieldOf("intervalTime").forGetter(GameState::intervalTime),
                    Codec.INT.fieldOf("roundTime").forGetter(GameState::roundTime),
                    Codec.list(BlockPos.CODEC).fieldOf("claimedLocations").forGetter(GameState::claimedLocations),
                ).apply(it, ::GameState)
        }

        val CODEC: Codec<GameState> = RecordCodecBuilder.create(codecDefinition)

        @JvmStatic
        fun fetch(server: MinecraftServer): GameState =
            with(server.getLevel(ServerLevel.OVERWORLD)) {
                return@with when (this) {
                    null -> GameState()
                    else -> this.dataStorage.computeIfAbsent(StorageRegistry.GAME_STATE)
                }
            }
    }
}
