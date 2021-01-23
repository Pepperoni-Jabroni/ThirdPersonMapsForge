package pepjebs.thirdpersonmaps.client.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.fml.client.gui.GuiUtils;

public class ThirdPersonMapsHUD extends GuiUtils {

    private static final ResourceLocation MAP_CHKRBRD =
            new ResourceLocation("minecraft:textures/map/map_background_checkerboard.png");
    private static Minecraft client;
    private static MapItemRenderer mapRenderer;

    public ThirdPersonMapsHUD() {
        client = Minecraft.getInstance();
        mapRenderer = client.gameRenderer.getMapItemRenderer();
    }

    public void render(MatrixStack matrices) {
        if (shouldDraw(client)) {
            if (client.player.getHeldItemMainhand().isItemEqual(new ItemStack(Items.FILLED_MAP))) {
                renderMapHUDFromItemStack(matrices, client.player.getHeldItemMainhand(), false);
            }
            if (client.player.getHeldItemOffhand().isItemEqual(new ItemStack(Items.FILLED_MAP))) {
                renderMapHUDFromItemStack(matrices, client.player.getHeldItemOffhand(), true);
            }
        }
    }

    private boolean shouldDraw(Minecraft client) {
        return client.gameSettings.getPointOfView() == PointOfView.THIRD_PERSON_BACK
                && !client.gameSettings.showDebugInfo;
    }

    private void renderMapHUDFromItemStack(MatrixStack matrices, ItemStack map, boolean isLeft) {
        if (client.world == null || client.player == null) return;
//        ThirdPersonMapsConfig conf = AutoConfig.getConfigHolder(ThirdPersonMapsConfig.class).getConfig();

        // Draw map background
        int y = 0;
        if (!isLeft) {
            // Handle potion effects on right-hand side of screen
            if (!client.player.getActivePotionMap().isEmpty()) {
                y = 26;
            }
        }
        MapData state = FilledMapItem.getData(map, client.world);
        int x = client.getMainWindow().getScaledWidth()-64;
        if (isLeft) {
            x = 0;
        }
        client.getTextureManager().bindTexture(MAP_CHKRBRD);
        drawContinuousTexturedBox(matrices,x,y,0,0, 64, 64, 64, 64, 0, 0);

        // Draw map data
        x += 4;
        y += 4;
        IRenderTypeBuffer.Impl vcp = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
        matrices.push();
        matrices.translate(x, y, 0.0);
        // Prepare yourself for some magic numbers
        matrices.scale((float) 64.0 / 142, (float) 64.0 / 142, 0);
        mapRenderer.renderMap(matrices, vcp, state, false, Integer.parseInt("0000000011110000", 2));
        vcp.finish();
        matrices.pop();
    }
}