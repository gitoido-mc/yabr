package lol.gito.yabr.event.server

import lol.gito.yabr.service.BattleRoyale
import net.minecraft.server.MinecraftServer

object EndServerTickEvent {
    operator fun invoke(server: MinecraftServer) {
        BattleRoyale.tickActive()
    }
}
