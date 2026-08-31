package lol.gito.yabr.net.packet.c2s

import lol.gito.yabr.Yabr
import lol.gito.yabr.service.EntityLocator
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.world.entity.projectile.EyeOfEnder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.gameevent.GameEvent

object LocatePlayerRequestC2SHandler {
    operator fun invoke(
        payload: LocatePlayerRequestC2S,
        context: ServerPlayNetworking.Context,
    ) {
        Yabr.info("c2s recv ${payload::class}")
        if (context.player().id != payload.entityId) return
        Yabr.info("same ids")
        val level = context.player().level()
        val player = context.player()

        val located =
            EntityLocator.locate(level, context.player()) ?: return
        Yabr.info("located someone ")
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
