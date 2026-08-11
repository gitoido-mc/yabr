package lol.gito.yabr.net

import lol.gito.yabr.Yabr
import lol.gito.yabr.Yabr.Companion.modId
import lol.gito.yabr.service.EntityLocator
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.world.entity.projectile.EyeOfEnder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.gameevent.GameEvent

object NetworkManager {
    class LocatePlayerRequestC2S(
        val entityId: Int,
    ) : CustomPacketPayload {
        companion object {
            val ID = modId("locate_player_request")
            val TYPE = CustomPacketPayload.Type<LocatePlayerRequestC2S>(ID)

            val CODEC =
                StreamCodec.composite(ByteBufCodecs.INT, LocatePlayerRequestC2S::entityId, ::LocatePlayerRequestC2S)
        }

        override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
    }

    fun register() {
        PayloadTypeRegistry.serverboundPlay().register(LocatePlayerRequestC2S.TYPE, LocatePlayerRequestC2S.CODEC)

        ServerPlayNetworking.registerGlobalReceiver(LocatePlayerRequestC2S.TYPE) { payload, context ->
            Yabr.logger.info("c2s recv ${payload::class}")
            if (context.player().id != payload.entityId) return@registerGlobalReceiver
            Yabr.logger.info("same ids")
            val level = context.player().level()
            val player = context.player()

            val located =
                EntityLocator.locate(level, context.player()) ?: return@registerGlobalReceiver
            Yabr.logger.info("located someone ")
            val eyeOfEnder = EyeOfEnder(level, player.x, player.getY(0.5), player.z)
            eyeOfEnder.item = ItemStack.EMPTY
            eyeOfEnder.signalTo(located.position())
            level.gameEvent(GameEvent.PROJECTILE_SHOOT, eyeOfEnder.position(), GameEvent.Context.of(player))

            val pitch = Mth.lerp(level.getRandom().nextFloat(), 0.33f, 0.5f)
            level.playSound(
                null,
                player.x,
                player.y,
                player.z,
                SoundEvents.ENDER_EYE_LAUNCH,
                SoundSource.NEUTRAL,
                1.0f,
                pitch,
            )
            level.addFreshEntity(eyeOfEnder)
        }
    }
}
