package lol.gito.yabr.service

import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.level.ServerPlayerGameMode
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.entity.player.Player

object EntityLocator {
    fun locate(
        level: ServerLevel,
        source: ServerPlayer,
    ): Player? = level.getNearestPlayer(TargetingConditions.forNonCombat().selector(predicate), source as LivingEntity)

    @JvmStatic
    private val predicate =
        TargetingConditions.Selector run@{ entity, level ->
            if (entity is ServerPlayer && !ServerPlayerGameMode(entity).isSurvival) return@run false
            if (!level.worldBorder.isWithinBounds(entity.position())) return@run false
            return@run true
        }
}
