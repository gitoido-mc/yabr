package lol.gito.yabr.event.server

import lol.gito.yabr.Yabr
import net.kyori.adventure.platform.modcommon.MinecraftServerAudiences
import net.minecraft.server.MinecraftServer

object ServerStartingEvent {
    operator fun invoke(server: MinecraftServer) {
        Yabr.adventure = MinecraftServerAudiences.of(server)
    }
}
