package lol.gito.yabr

import lol.gito.yabr.event.EventManager
import lol.gito.yabr.net.NetworkManager
import net.fabricmc.api.ModInitializer
import net.minecraft.resources.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Yabr : ModInitializer {
    companion object {
        const val MOD_ID = "yabr"

        private val logger: Logger = LoggerFactory.getLogger(MOD_ID)

        @JvmStatic
        fun modId(path: String) = Identifier.fromNamespaceAndPath(MOD_ID, path)

        @JvmStatic
        fun tlk(
            path: String,
            prefix: String?,
        ) = when (prefix) {
            null -> "$MOD_ID.$path"
            else -> "$prefix.$MOD_ID.$path"
        }

        fun info(
            message: String,
            vararg args: Any,
        ) = logger.info("YABR :: $message", *args)
    }

    override fun onInitialize() {
        NetworkManager()
        EventManager()
    }
}
