package lol.gito.yabr.client.control

import com.mojang.blaze3d.platform.InputConstants
import lol.gito.yabr.Yabr.Companion.modId
import lol.gito.yabr.Yabr.Companion.tlk
import lol.gito.yabr.net.NetworkManager
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft

object KeyBindingRegistry {
    const val LOCATOR_COOLDOWN = 1200

    @JvmStatic
    val CATEGORY: KeyMapping.Category = KeyMapping.Category.register(modId("controls"))

    @JvmStatic
    val LOCATOR: KeyMapping =
        KeyMappingHelper.registerKeyMapping(
            KeyMapping(
                tlk("locate", "key"),
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_R,
                CATEGORY,
            ),
        )

    val cooldowns = mutableMapOf<KeyMapping, Int>()

    fun register() {
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            cooldowns.forEach { (mapping, i) ->
                if (i > 0) cooldowns[mapping] = i - 1
                if (i == 0) cooldowns.remove(mapping)
            }

            maybePlayerLocatorRequest(client)
        }
    }

    private fun maybePlayerLocatorRequest(client: Minecraft) {
        while (this.LOCATOR.consumeClick()) {
            if (client.player == null) return
//            if (!cooldowns.contains(this.LOCATOR)) {
//                this.cooldowns[this.LOCATOR] = LOCATOR_COOLDOWN

            if (client.player != null) {
                ClientPlayNetworking.send(NetworkManager.LocatePlayerRequestC2S(client.player!!.id))
            }
//            }
        }
    }
}
