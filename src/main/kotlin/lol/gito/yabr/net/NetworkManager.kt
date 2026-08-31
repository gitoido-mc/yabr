package lol.gito.yabr.net

import lol.gito.yabr.net.packet.c2s.LocatePlayerRequestC2S
import lol.gito.yabr.net.packet.c2s.LocatePlayerRequestC2SHandler
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object NetworkManager {
    operator fun invoke() {
        PayloadTypeRegistry.serverboundPlay().register(LocatePlayerRequestC2S.TYPE, LocatePlayerRequestC2S.CODEC)
        ServerPlayNetworking.registerGlobalReceiver(LocatePlayerRequestC2S.TYPE, LocatePlayerRequestC2SHandler::invoke)
    }
}
