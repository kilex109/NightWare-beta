package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import com.jhlabs.vecmath.Vector4f;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import nightware.main.NightWare;
import nightware.main.event.render.EventRender2D;
import nightware.main.event.render.EventRender3D;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.BooleanSetting;
import nightware.main.module.setting.impl.ColorSetting;
import nightware.main.module.setting.impl.NumberSetting;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.font.Fonts;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ModuleAnnotation(name = "BotESP", category = Category.RENDER)
public class ZombaESP extends Module {
   public static ColorSetting defaultBot = new ColorSetting("Цвет", new Color(13, 65, 236, 140).getRGB());
   public static NumberSetting lineWidth = new NumberSetting("Ширина линий", 0.1f, 0.1f, 2, 0.1f);
   public static BooleanSetting fill = new BooleanSetting("Заливка", true);
   public static BooleanSetting outline = new BooleanSetting("Обводка", true);

   @EventTarget
   public void onRender3D(EventRender3D eventRender3D) {
      for (Entity entity : mc.world.loadedEntityList) {
         if (entity == null) return;
         if (entity instanceof EntityZombie)
            RenderUtility.drawBoxESP(entity.getEntityBoundingBox(), defaultBot.getColor(), lineWidth.get(), outline.get(), fill.get(), defaultBot.getColor().getAlpha(), defaultBot.getColor().getAlpha());
         if (entity instanceof EntityPigZombie)
            RenderUtility.drawBoxESP(entity.getEntityBoundingBox(), defaultBot.getColor(), lineWidth.get(), outline.get(), fill.get(), defaultBot.getColor().getAlpha(), defaultBot.getColor().getAlpha());
      }
   }
}