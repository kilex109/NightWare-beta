package nightware.main.utility.render.model;

import nightware.main.NightWare;
import nightware.main.module.impl.render.Wings;
import nightware.main.utility.Utility;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class WingsLayerRender implements LayerRenderer<AbstractClientPlayer>, Utility {
   public void doRenderLayer(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      if (player == mc.player) {
         if (NightWare.getInstance().getModuleManager().getModule(Wings.class).isEnabled()) {
            if (player.hasPlayerInfo() && !player.isInvisible()) {
               WingsModel.renderWings(player, partialTicks, (double)Wings.scale.get());
            }
         }
      }
   }

   public boolean shouldCombineTextures() {
      return false;
   }
}
