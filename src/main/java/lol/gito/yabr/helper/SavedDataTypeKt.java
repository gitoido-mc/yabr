package lol.gito.yabr.helper;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.function.Supplier;

public class SavedDataTypeKt {
    public static <T extends SavedData> SavedDataType<T> build(Identifier id, Supplier<T> constuctor, Codec<T> codec) {
        return new SavedDataType<>(id, constuctor, codec, null);
    }
}
