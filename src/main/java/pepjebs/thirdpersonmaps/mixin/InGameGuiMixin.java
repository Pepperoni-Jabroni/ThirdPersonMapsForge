package pepjebs.thirdpersonmaps.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.IngameGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pepjebs.thirdpersonmaps.client.ui.ThirdPersonMapsHUD;

@Mixin(IngameGui.class)
public class InGameGuiMixin {
    private static ThirdPersonMapsHUD mapHUD = new ThirdPersonMapsHUD();

    @Inject(
            method = "renderIngameGui",
            at = @At("TAIL")
    )
    private void renderThirdPersonMap(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        mapHUD.render(matrices);
    }
}
