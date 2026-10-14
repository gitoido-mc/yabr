package lol.gito.yabr.service

import lol.gito.yabr.state.GameState
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import kotlin.math.max
import kotlin.math.min

class BattleRoyale(
    val roundTime: Int,
    val intervalTime: Int,
    val center: BlockPos,
    val radius: Int,
    val players: MutableList<ServerPlayer> = mutableListOf(),
) {
    constructor(
        state: GameState,
        players: MutableList<ServerPlayer> = mutableListOf(),
    ) : this(state.roundTime, state.intervalTime, state.center, state.radius, players)

    var ticksPassed = 0
        private set

    private var level: ServerLevel? = null
    private var nextShrinkTick = 0
    private var currentTargetSize = 0.0

    fun addPlayer(player: ServerPlayer) = players.add(player)

    fun start(level: ServerLevel): Boolean {
        if (activeRound != null && activeRound !== this) return false

        require(roundTime > 0) { "Round time must be greater than zero ticks" }
        require(intervalTime > 0) { "Border shrink interval must be greater than zero ticks" }
        require(radius > 0) { "Initial border radius must be greater than zero" }

        this.level = level
        ticksPassed = 0
        val border = level.worldBorder
        border.setCenter(center.x.toDouble(), center.z.toDouble())
        currentTargetSize = radius * 2.0
        border.size = currentTargetSize

        PlayerTeleporter.spreadPlayers(level, players)

        activeRound = this
        scheduleNextShrink(0)
        return true
    }

    fun tick() {
        val currentLevel = level ?: return
        if (ticksPassed >= roundTime) return

        val alivePlayers = players.filter { it in currentLevel.players() && !it.isDeadOrDying }
        if (alivePlayers.size <= 1) {
            alivePlayers.singleOrNull()?.let { winner ->
                currentLevel.server.playerList.broadcastSystemMessage(
                    Component.translatable("message.yabr.battle_winner", winner.name),
                    false,
                )
                PlayerTeleporter.teleportPlayer(currentLevel, winner, center)
            }
            teardown()
            return
        }

        ticksPassed++
        if (ticksPassed < nextShrinkTick) return

        if (ticksPassed >= roundTime) {
            currentLevel.worldBorder.size = MIN_BORDER_SIZE
            teardown()
        } else {
            scheduleNextShrink(ticksPassed)
        }
    }

    fun teardown() {
        level?.worldBorder?.size = level?.worldBorder?.absoluteMaxSize?.toDouble() ?: 29999984.0
        if (activeRound === this) activeRound = null
        level = null
    }

    private fun scheduleNextShrink(elapsedTicks: Int) {
        val currentLevel = checkNotNull(level)
        val segmentTicks = min(intervalTime, roundTime - elapsedTicks)
        val targetElapsedTicks = elapsedTicks + segmentTicks
        val nextTargetSize =
            max(
                MIN_BORDER_SIZE,
                radius * 2.0 * (roundTime - targetElapsedTicks).toDouble() / roundTime,
            )

        currentLevel.worldBorder.lerpSizeBetween(
            currentTargetSize,
            nextTargetSize,
            segmentTicks.toLong(),
            currentLevel.gameTime,
        )
        currentTargetSize = nextTargetSize
        nextShrinkTick = targetElapsedTicks
    }

    companion object {
        private const val MIN_BORDER_SIZE = 32.0
        private var activeRound: BattleRoyale? = null

        fun tickActive() {
            activeRound?.tick()
        }

        fun teardownActive() {
            activeRound?.teardown()
        }
    }
}
