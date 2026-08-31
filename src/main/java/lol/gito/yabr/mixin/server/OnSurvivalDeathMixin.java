package lol.gito.yabr.mixin.server;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class OnSurvivalDeathMixin {

    @Inject(method = "die", at = @At("TAIL"))
    public void yabr$switchGamemode(DamageSource source, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        if (player.permissions().hasPermission(Permissions.COMMANDS_MODERATOR)) return;
        player.setGameMode(GameType.ADVENTURE);
    }
}
