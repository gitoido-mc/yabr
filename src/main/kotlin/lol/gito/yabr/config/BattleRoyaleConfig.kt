package lol.gito.yabr.config

import lol.gito.yabr.Yabr
import lol.gito.yabr.Yabr.Companion.tlc
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.client.gui.screens.Screen

object BattleRoyaleConfig {
    fun build(): Screen? {
        val builder =
            ConfigBuilder
                .create()
                .setParentScreen(null)
                .setTitle(tlc("configurator", "title"))

        val entryBuilder = builder.entryBuilder()

        val general = builder.getOrCreateCategory(tlc("general", "category"))

        val radius =
            entryBuilder
                .startIntSlider(tlc("radius", "option"), 256, 256, 8192)
                .build()

        val roundTime =
            entryBuilder
                .startIntList(tlc("round_time", "option"), listOf(5, 10, 15, 20, 30, 45, 60))
                .build()

        val intervalTime =
            entryBuilder
                .startIntList(tlc("interval_time", "option"), listOf(10, 20, 30))
                .build()

        general.addEntry(radius)
        general.addEntry(roundTime)
        general.addEntry(intervalTime)

        return builder.build()
    }
}
