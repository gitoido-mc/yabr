package lol.gito.yabr.service.action

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel

class PlaceLobby(
    val level: ServerLevel,
    val position: BlockPos,
) : ActionInterface {
    override fun run() {
    }
}
