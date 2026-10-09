package lol.gito.yabr.event.server

import lol.gito.yabr.Yabr
import lol.gito.yabr.state.GameState
import net.minecraft.server.MinecraftServer

object ServerStartedEvent {
    operator fun invoke(server: MinecraftServer) {
        Yabr.server = server
        Yabr.state = GameState.fetch(server)
    }
}
