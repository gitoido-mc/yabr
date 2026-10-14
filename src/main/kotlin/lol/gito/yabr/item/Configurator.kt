package lol.gito.yabr.item

import lol.gito.yabr.Yabr
import lol.gito.yabr.Yabr.Companion.modId
import lol.gito.yabr.Yabr.Companion.tlk
import lol.gito.yabr.config.BattleRoyaleConfig
import lol.gito.yabr.service.BattleRoyale
import lol.gito.yabr.service.PlayerTeleporter
import lol.gito.yabr.service.action.PlaceStructure
import lol.gito.yabr.service.action.PregenerateField
import lol.gito.yabr.state.GameState.Companion.CLAIM_EXCLUSION_RADIUS
import net.kyori.adventure.text.Component
import net.minecraft.client.Minecraft
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level

class Configurator(
    properties: Properties,
) : Item(properties) {
    override fun postHurtEnemy(
        itemStack: ItemStack,
        mob: LivingEntity,
        attacker: LivingEntity,
    ) {
        if (mob !is ServerPlayer && attacker !is ServerPlayer) return
        if (!(attacker as ServerPlayer).isCreative) return

        mob.addEffect(MobEffectInstance(MobEffects.REGENERATION, 1 * 20, 5))
        mob.addEffect(MobEffectInstance(MobEffects.GLOWING, 300 * 20))
    }

    override fun use(
        level: Level,
        player: Player,
        hand: InteractionHand,
    ): InteractionResult {
        if (player.isCrouching) {
            if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS
            return startBattleRoyale(level, player)
        }

        if (level.isClientSide) {
            if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS
            Minecraft.getInstance().gui.setScreen(BattleRoyaleConfig.build())
            return InteractionResult.SUCCESS
        }

        return InteractionResult.SUCCESS
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val player = context.player ?: return InteractionResult.PASS
        if (context.hand == InteractionHand.MAIN_HAND && player.isCrouching) {
            return startBattleRoyale(context.level, player)
        }

        if (context.level.isClientSide) {
            return InteractionResult.SUCCESS
        }

        if (context.hand == InteractionHand.MAIN_HAND) {
            if (context.level.getBlockState(context.clickedPos).isAir) {
                return InteractionResult.PASS
            }

            if (!Yabr.state.claimPosition(context.clickedPos, CLAIM_EXCLUSION_RADIUS)) {
                Yabr.adventure
                    .audience(context.player as ServerPlayer)
                    .sendMessage(Component.translatable(tlk("pick_other_place", "message")))

                return InteractionResult.FAIL
            }

            Yabr.state.center = context.clickedPos.atY(266).south(13).east(13)
            PlaceStructure(modId("lobby"), context.level as ServerLevel, context.clickedPos).run()
            PregenerateField(context.level as ServerLevel, context.clickedPos, Yabr.state.radius).run()
            PlayerTeleporter.teleportPlayer(
                context.level as ServerLevel,
                context.player as ServerPlayer,
                context.clickedPos,
            )
        }

        return InteractionResult.SUCCESS_SERVER
    }

    private fun startBattleRoyale(
        level: Level,
        player: Player,
    ): InteractionResult {
        if (level.isClientSide) return InteractionResult.SUCCESS

        val serverPlayer = player as? ServerPlayer ?: return InteractionResult.FAIL
        val serverLevel = level as? ServerLevel ?: return InteractionResult.FAIL
        val round = BattleRoyale(Yabr.state, Yabr.server.playerList.players.toMutableList())
        if (!round.start(serverLevel)) {
            Yabr.adventure
                .audience(serverPlayer)
                .sendMessage(Component.translatable(tlk("battle_already_started", "message")))
        }

        return InteractionResult.SUCCESS
    }
}
