package lol.gito.yabr.net.packet.c2s

import lol.gito.yabr.Yabr.Companion.modId
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class LocatePlayerRequestC2S(
    val entityId: Int,
) : CustomPacketPayload {
    companion object {
        val ID = modId("locate_player_request")
        val TYPE = CustomPacketPayload.Type<LocatePlayerRequestC2S>(ID)
        val CODEC = StreamCodec.composite(ByteBufCodecs.INT, LocatePlayerRequestC2S::entityId, ::LocatePlayerRequestC2S)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}
