package lol.gito.yabr.event.server

import lol.gito.yabr.Yabr
import lol.gito.yabr.Yabr.Companion.PARTICIPANTS_TEAM
import net.minecraft.server.MinecraftServer

object ServerStoppedEvent {
    operator fun invoke(server: MinecraftServer) {
        Yabr.adventure.close()
    }
}
