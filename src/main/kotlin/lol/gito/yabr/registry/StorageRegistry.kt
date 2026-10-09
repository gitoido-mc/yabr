package lol.gito.yabr.registry

import lol.gito.yabr.Yabr
import lol.gito.yabr.helper.SavedDataTypeKt
import lol.gito.yabr.state.GameState
import net.minecraft.core.registries.Registries
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.world.level.saveddata.SavedDataType

object StorageRegistry {
    val GAME_STATE: SavedDataType<GameState> =
        SavedDataTypeKt.build(
            Yabr.modId("game_state"),
            ::GameState,
            GameState.CODEC,
        )

    operator fun invoke() {
        Yabr.info("Storage registry init")
    }
}
