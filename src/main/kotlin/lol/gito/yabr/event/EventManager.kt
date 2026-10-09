package lol.gito.yabr.event

import lol.gito.yabr.event.server.EndServerTickEvent
import lol.gito.yabr.event.server.ServerStartedEvent
import lol.gito.yabr.event.server.ServerStartingEvent
import lol.gito.yabr.event.server.ServerStoppedEvent
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents

object EventManager {
    operator fun invoke() {
        ServerLifecycleEvents.SERVER_STARTING.register { ServerStartingEvent(it) }
        ServerLifecycleEvents.SERVER_STARTED.register { ServerStartedEvent(it) }
        ServerLifecycleEvents.SERVER_STOPPED.register { ServerStoppedEvent(it) }
        ServerTickEvents.END_SERVER_TICK.register { EndServerTickEvent(it) }
    }
}
