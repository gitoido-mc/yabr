package lol.gito.yabr.event.server

import lol.gito.yabr.Yabr
import lol.gito.yabr.state.GameState
import net.minecraft.server.MinecraftServer

object LoadServerEvent {
    operator fun invoke(server: MinecraftServer) {
        Yabr.info("Server started!")
        val state = GameState.fetch(server)
        Yabr.info("checking center: ${state.center}")
    }
}
