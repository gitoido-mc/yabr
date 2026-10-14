package lol.gito.yabr.event.server

import lol.gito.yabr.Yabr
import lol.gito.yabr.service.BattleRoyale
import net.minecraft.server.MinecraftServer

object ServerStoppedEvent {
    operator fun invoke(server: MinecraftServer) {
        BattleRoyale.teardownActive()
        Yabr.adventure.close()
    }
}
