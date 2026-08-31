package lol.gito.yabr.mixin.enumeration;

import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.fixes.References;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DataFixTypes.class)
enum DataFixerRefs {
    YABR_GAME_STATE(References.reference("saved_data/yabr_game_state"));

    @Shadow
    private DataFixerRefs(DSL.TypeReference type) {
    }
}
