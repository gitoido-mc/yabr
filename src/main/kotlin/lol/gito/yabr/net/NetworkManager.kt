package lol.gito.yabr.net

import lol.gito.yabr.net.packet.client.LocatePlayerRequest
import lol.gito.yabr.net.packet.client.LocatePlayerRequestHandler
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object NetworkManager {
    operator fun invoke() {
        PayloadTypeRegistry.serverboundPlay().register(LocatePlayerRequest.TYPE, LocatePlayerRequest.CODEC)
        ServerPlayNetworking.registerGlobalReceiver(LocatePlayerRequest.TYPE, LocatePlayerRequestHandler::invoke)
    }
}
