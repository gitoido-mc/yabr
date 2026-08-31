package lol.gito.yabr.registry

import lol.gito.yabr.Yabr
import lol.gito.yabr.state.GameState
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.world.level.saveddata.SavedDataType

object StorageRegistry {
    val GAME_STATE: SavedDataType<GameState> =
        SavedDataType(
            Yabr.modId("game_state"),
            ::GameState,
            GameState.CODEC,
            null!!,
        )

    operator fun invoke() {
        Yabr.info("Storage registry init")
    }
}
