package lol.gito.yabr.service

import lol.gito.yabr.Yabr
import lol.gito.yabr.Yabr.Companion.PARTICIPANTS_TEAM
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer

class BattleRoyale(
    val roundTime: Int,
    val intervalTime: Int,
    val center: BlockPos,
    val radius: Int,
    val players: MutableList<ServerPlayer> = mutableListOf(),
) {
    var ticksPassed = 0

    fun addPlayer(player: ServerPlayer) = players.add(player)

    fun start(level: ServerLevel) {
        PlayerTeleporter
            .spreadPlayers(level, players, center, 256, radius, 256)
    }

    fun tick() {
        if (ticksPassed == roundTime * 20) {
            return
        }

        ticksPassed++
    }

    fun teardown() {
    }
}
