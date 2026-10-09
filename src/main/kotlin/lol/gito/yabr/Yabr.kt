package lol.gito.yabr

import lol.gito.yabr.event.EventManager
import lol.gito.yabr.net.NetworkManager
import lol.gito.yabr.registry.ItemRegistry
import lol.gito.yabr.state.GameState
import net.fabricmc.api.ModInitializer
import net.kyori.adventure.platform.modcommon.MinecraftServerAudiences
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.server.MinecraftServer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Yabr : ModInitializer {
    companion object {
        const val MOD_ID = "yabr"
        const val PARTICIPANTS_TEAM = "team.yabr.participants"
        private val logger: Logger = LoggerFactory.getLogger(MOD_ID)
        lateinit var server: MinecraftServer
        lateinit var adventure: MinecraftServerAudiences
        lateinit var state: GameState

        @JvmStatic
        fun modId(path: String): Identifier = Identifier.fromNamespaceAndPath(MOD_ID, path)

        @JvmStatic
        fun tlk(
            path: String,
            prefix: String?,
        ) = when (prefix) {
            null -> "$MOD_ID.$path"
            else -> "$prefix.$MOD_ID.$path"
        }

        fun tlc(
            path: String,
            prefix: String?,
        ): Component = Component.translatable(tlk(path, prefix))

        fun info(
            message: String,
            vararg args: Any,
        ) = logger.info("YABR :: $message", *args)
    }

    override fun onInitialize() {
        ItemRegistry()
        NetworkManager()
        EventManager()
    }
}
