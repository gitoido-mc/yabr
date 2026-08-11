package lol.gito.yabr.service

import lol.gito.yabr.Yabr
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.level.ServerPlayerGameMode
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.entity.player.Player
import kotlin.compareTo

object EntityLocator {
    fun locate(
        level: ServerLevel,
        source: ServerPlayer,
    ): Player? = level.getNearestPlayer(TargetingConditions.forNonCombat().selector(predicate), source as LivingEntity)

    @JvmStatic
    private val predicate =
        TargetingConditions.Selector { entity, level ->
            Yabr.logger.info("entity: ${entity.name}")
            Yabr.logger.info("checking survival and player")
            if (entity is ServerPlayer && !ServerPlayerGameMode(entity).isSurvival) return@Selector false
            Yabr.logger.info("checking in world borders")
            if (!level.worldBorder.isWithinBounds(entity.position())) return@Selector false
            Yabr.logger.info("checking Y")
            if (entity.y >= 300) return@Selector false
            Yabr.logger.info("all passed")
            return@Selector true
        }
}
