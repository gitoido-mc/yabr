package lol.gito.yabr.event

import lol.gito.yabr.event.server.LoadServerEvent
import lol.gito.yabr.event.server.TickServerEvent
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents

object EventManager {
    operator fun invoke() {
        ServerLifecycleEvents.SERVER_STARTED.register { LoadServerEvent(it) }
        ServerTickEvents.END_SERVER_TICK.register { TickServerEvent(it) }
    }
}
