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

class GameState() : SavedData() {
    constructor(
        center: BlockPos,
        difficulty: Difficulty,
        radius: Int,
        intervalTime: Int,
        roundTime: Int,
    ) : this() {
        this.center = center
        this.difficulty = difficulty
        this.radius = radius
        this.intervalTime = intervalTime
        this.roundTime = roundTime
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
    var radius: Int = 160
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
}
