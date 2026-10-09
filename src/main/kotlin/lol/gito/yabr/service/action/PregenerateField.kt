package lol.gito.yabr.service.action

import lol.gito.yabr.Yabr
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import org.popcraft.chunky.ChunkyProvider

class PregenerateField(
    val level: ServerLevel,
    val center: BlockPos,
    val radius: Int,
) : ActionInterface {
    override fun run() {
        val chunky = ChunkyProvider.get().api
        val levelName = level.dimension().identifier().toString()
        Yabr.info("chunky version: ${chunky.version()}")
        if (chunky.version() == 0 && !chunky.isRunning(levelName)) {
            chunky.startTask(
                levelName,
                "square",
                center.x.toDouble(),
                center.z.toDouble(),
                radius.toDouble(),
                radius.toDouble(),
                "concentric",
            )
        }
    }
}
