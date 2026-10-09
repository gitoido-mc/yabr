package lol.gito.yabr.client

import lol.gito.yabr.client.control.KeyBindingRegistry
import net.fabricmc.api.ClientModInitializer
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences

class YabrClient : ClientModInitializer {
    var audience = MinecraftClientAudiences.of().audience()

    override fun onInitializeClient() {
        KeyBindingRegistry.register()
    }
}
