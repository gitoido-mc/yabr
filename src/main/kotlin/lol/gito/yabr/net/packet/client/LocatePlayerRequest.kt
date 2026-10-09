package lol.gito.yabr.net.packet.client

import io.netty.buffer.ByteBuf
import lol.gito.yabr.Yabr.Companion.modId
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

data class LocatePlayerRequest(
    val entityId: Int,
) : CustomPacketPayload {
    companion object {
        val ID = modId("locate_player_request")
        val TYPE = CustomPacketPayload.Type<LocatePlayerRequest>(ID)
        val CODEC: StreamCodec<ByteBuf, LocatePlayerRequest> =
            StreamCodec.composite(
                ByteBufCodecs.INT,
                LocatePlayerRequest::entityId,
                ::LocatePlayerRequest,
            )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}
