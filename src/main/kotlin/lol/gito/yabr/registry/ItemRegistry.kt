package lol.gito.yabr.registry

import lol.gito.yabr.Yabr
import lol.gito.yabr.item.Configurator
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.Item

object ItemRegistry {
    @JvmStatic
    val CONFIGURATOR_KEY: ResourceKey<Item> = create("configurator")

    val CONFIGURATOR = register(CONFIGURATOR_KEY, ::Configurator, Item.Properties())

    fun create(name: String): ResourceKey<Item> = ResourceKey.create(Registries.ITEM, Yabr.modId(name))

    fun register(
        itemKey: ResourceKey<Item>,
        itemFactory: (Item.Properties) -> Item,
        settings: Item.Properties,
    ): Item {
        val item: Item = itemFactory.invoke(settings.setId(itemKey))

        Registry.register(BuiltInRegistries.ITEM, itemKey, item)

        return item
    }

    operator fun invoke() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.OP_BLOCKS).register {
            it.accept(CONFIGURATOR)
        }
    }
}
