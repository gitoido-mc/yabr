package lol.gito.yabr.client

import lol.gito.yabr.client.control.KeyBindingRegistry
import net.fabricmc.api.ClientModInitializer

class YabrClient : ClientModInitializer {
    override fun onInitializeClient() {
        KeyBindingRegistry.register()
    }
}
