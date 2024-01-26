package nightware.main.utility.render.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import nightware.main.NightWare;
import nightware.main.utility.render.ColorUtility;
import org.lwjgl.opengl.GL11;

public class WingsModel extends ModelBase {
   private static final ResourceLocation location = new ResourceLocation("nightware/models/wings.png");
   private static ModelRenderer wingTip;
   private static ModelRenderer wing;

   public WingsModel() {
      this.setTextureOffset("wing.bone", 0, 0);
      this.setTextureOffset("wing.skin", -10, 8);
      this.setTextureOffset("wingtip.bone", 0, 5);
      this.setTextureOffset("wingtip.skin", -10, 18);
      (wing = new ModelRenderer(this, "wing")).setTextureSize(30, 30);
      wing.setRotationPoint(-2.0F, 0.0F, 0.0F);
      wing.addBox("bone", -10.0F, -1.0F, -1.0F, 10, 2, 2);
      wing.addBox("skin", -10.0F, 0.0F, 0.5F, 10, 0, 10);
      (wingTip = new ModelRenderer(this, "wingtip")).setTextureSize(30, 30);
      wingTip.setRotationPoint(-10.0F, 0.0F, 0.0F);
      wingTip.addBox("bone", -10.0F, -0.5F, -0.5F, 10, 1, 1);
      wingTip.addBox("skin", -10.0F, 0.0F, 0.5F, 10, 0, 10);
      wing.addChild(wingTip);
   }

   public static void renderWings(EntityPlayer player, float partialTicks, double scale) {
      double rotate = (double)interpolate(player.prevRenderYawOffset, player.renderYawOffset, partialTicks);
      GL11.glPushMatrix();
      GL11.glScaled(scale, scale, scale);
      GL11.glRotated(Math.toRadians(rotate) - 4.0D, 0.0D, 1.0D, 0.0D);
      GL11.glTranslated(0.0D, 0.1D, 0.095D);
      if (player.isSneaking()) {
         GL11.glTranslated(0.0D, 0.2D, 0.05D);
      }

      if (!((ItemStack)player.inventory.armorInventory.get(2)).isEmpty()) {
         GL11.glTranslated(0.0D, 0.0D, 0.05D);
      }

      Minecraft.getMinecraft().getTextureManager().bindTexture(location);

      for(int i = 0; i < 2; ++i) {
         GL11.glEnable(2884);
         float f11 = (float)(System.currentTimeMillis() % 1000L) / 1000.0F * 3.1415927F * 2.0F;
         wing.rotateAngleX = (float)(Math.toRadians(-80.0D) - Math.cos((double)f11) * 0.20000000298023224D);
         wing.rotateAngleY = (float)(Math.toRadians(30.0D) + Math.sin((double)f11) * 0.4000000059604645D);
         wing.rotateAngleZ = (float)Math.toRadians(20.0D);
         wingTip.rotateAngleZ = (float)(-(Math.sin((double)(f11 + 2.0F)) + 0.5D) * 0.75D);
         wing.render(0.0625F);
         GL11.glScalef(-1.0F, 1.0F, 1.0F);
         if (i == 0) {
            GL11.glCullFace(1028);
         }
      }

      GL11.glCullFace(1029);
      GL11.glDisable(2884);
      GL11.glPopMatrix();
   }

   private static float interpolate(float current, float target, float percent) {
      float f = (current + (target - current) * percent) % 360.0F;
      return f < 0.0F ? f + 360.0F : f;
   }
}
