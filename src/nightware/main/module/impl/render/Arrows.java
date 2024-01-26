package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import nightware.main.NightWare;
import nightware.main.event.render.EventRender2D;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.NumberSetting;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.blur.BlurUtility;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@ModuleAnnotation(
   name = "Arrows",
   category = Category.RENDER
)
public class Arrows extends Module {

   @EventTarget
   public void onDispay(EventRender2D eventDisplay) {
      for (Entity entity : mc.world.loadedEntityList) {
         if (entity != mc.player && entity instanceof EntityPlayer) {
            GlStateManager.pushMatrix();
            Color color = !NightWare.getInstance().getFriendManager().isFriend(entity.getName()) ? NightWare.getInstance().getC(0) : Color.GREEN;
            double yaw = Math.toDegrees(Math.atan2(entity.lastTickPosZ - mc.player.posZ, entity.lastTickPosX - mc.player.posX)) - mc.player.rotationYaw - 90;
            GL11.glTranslated((new ScaledResolution(mc)).getScaledWidth() / 2d + 1d, (new ScaledResolution(mc)).getScaledHeight() / 2d, 0);
            GL11.glTranslated((Math.cos(Math.toRadians(yaw - 90)) * 20), Math.sin(Math.toRadians(yaw - 90)) * 20, 0);
            GL11.glRotated(yaw, 0, 0, 1);
            RenderUtility.drawImage(new ResourceLocation("nightware/textures/triangle.png"), (int) -9.9f, 0, (int) 17.5f, (int) 17.5f, color.getRGB());
            GlStateManager.popMatrix();
         }
      }
   }
}