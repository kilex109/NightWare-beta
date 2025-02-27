package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import nightware.main.AnimationUtils;
import nightware.main.NightWare;
import nightware.main.event.misc.EventKeyBoard;
import nightware.main.event.packet.EventReceivePacket;
import nightware.main.event.render.EventRender2D;
import nightware.main.event.render.EventRender3D;
import nightware.main.manager.dragging.DragManager;
import nightware.main.manager.dragging.Draggable;
import nightware.main.manager.theme.Themes;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.Utils;
import nightware.main.module.setting.impl.ModeSetting;
import nightware.main.utility.render.AntiAliasingUtility;
import nightware.main.utility.render.ColorUtility;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.font.Fonts;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

@ModuleAnnotation(
   name = "SoundInfo",
   category = Category.RENDER
)
public class SoundInfo extends Module {
   public static ModeSetting tracerMode = new ModeSetting("����� ��������", "�������", "�������", "�����");
   private final Draggable siDraggable = DragManager.create(this, "Sound Info", 250, 100);
   int chouse = -1;
   AnimationUtils animChouse;
   AnimationUtils animSize;
   ArrayList<Ex> exes = new ArrayList<>();

   public SoundInfo() {
      animChouse = new AnimationUtils(0, 0, 0.1f);
      animSize = new AnimationUtils(15, 15, 0.1f);
   }

   @EventTarget
   public void keyBoard(EventKeyBoard event) {
      if (exes.size() > 0) {
         if (event.getKey() == Keyboard.KEY_UP) {
            if (chouse == -1) {
               chouse = exes.size() - 1;
            } else {
               chouse--;
            }
         }
         if (event.getKey() == Keyboard.KEY_DOWN) {
            if (chouse == exes.size() - 1) {
               chouse = -1;
            } else {
               chouse++;
            }
         }
         if (event.getKey() == Keyboard.KEY_LEFT) {
            if (chouse == -1) {
               for (Ex ex : exes) {
                  ex.remove();
               }
            } else {
               exes.get(chouse).remove();
            }
         }
         if (event.getKey() == Keyboard.KEY_RIGHT) {
            if (chouse == -1) {

            } else {
               exes.get(chouse).use = !exes.get(chouse).use;
            }
         }
      }
   }

   @EventTarget
   public void onPacket(EventReceivePacket event) {
      if (event.getPacket() instanceof SPacketCustomSound) {
         SPacketCustomSound packet = (SPacketCustomSound) event.getPacket();
         if (mc.player.getDistanceSq(packet.getX(), packet.getY(), packet.getZ()) > 5) {

            if (((packet.getSoundName().contains("shoot") || packet.getSoundName().contains("explosion") || (packet.getSoundName().contains("separate") && packet.getSoundName().contains("grenade_launcher"))) && !packet.getSoundName().contains("prepare")) && !packet.getSoundName().contains("fail") && !packet.getSoundName().contains("rocket_launcher")) {
               if (exes.size() > 0) {
                  boolean g = true;
                  for (Ex ex : exes) {
                     if (ex.id.replaceAll(".3p", "").equals(packet.getSoundName().replaceAll(".3p", "")) && Utils.getDistance(new Vec3d(packet.getX(), packet.getY(), packet.getZ()), ex.pos) < 80) {
                        ex.count++;
                        g = false;
                     }
                  }
                  if (g && exes.size() <= 20) {
                     exes.add(new Ex(new Vec3d(packet.getX(), packet.getY(), packet.getZ()), packet.getSoundName()));
                  }
               } else {
                  if (exes.size() <= 20) {
                     exes.add(new Ex(new Vec3d(packet.getX(), packet.getY(), packet.getZ()), packet.getSoundName()));
                  }
               }

            }
         }

      }
   }

   @EventTarget
   public void onRender3D(EventRender3D event) {
      this.siDraggable.setWidth(125);
      if (tracerMode.is("�����")) {
         RenderUtility.glColor(Color.white.getRGB());
         mc.gameSettings.viewBobbing = false;
         mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 2);
         GL11.glPushMatrix();
         GL11.glEnable(2848);
         GL11.glDisable(2929);
         GL11.glDisable(3553);
         GL11.glDisable(2896);
         GL11.glDepthMask(false);
         GL11.glBlendFunc(770, 771);
         GL11.glEnable(GL11.GL_LINE_SMOOTH);
         GL11.glLineWidth(0.000001f);
         for (Ex ex : exes) {
            if (ex.use) {
               Color color = Color.WHITE;
               double x = ex.pos.xCoord - mc.getRenderManager().viewerPosX;
               double y = ex.pos.yCoord - mc.getRenderManager().viewerPosY;
               double z = ex.pos.zCoord - mc.getRenderManager().viewerPosZ;
               float red = color.getRed() / 255F;
               float green = color.getGreen() / 255F;
               float blue = color.getBlue() / 255F;
               float alpha = color.getAlpha() / 255F;
               GL11.glPushMatrix();
               boolean old = mc.gameSettings.viewBobbing;
               mc.gameSettings.viewBobbing = false;
               mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 2);
               mc.gameSettings.viewBobbing = old;
               GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
               GL11.glEnable(GL11.GL_BLEND);
               GL11.glLineWidth(1f);
               GL11.glDisable(GL11.GL_TEXTURE_2D);
               GL11.glDisable(GL11.GL_DEPTH_TEST);
               AntiAliasingUtility.hook(true, false, false);
               GL11.glDepthMask(false);
               GlStateManager.color(red, green, blue, alpha);
               GL11.glBegin(GL11.GL_LINE_STRIP);
               Vec3d vec = new Vec3d(0, 0, 1).rotatePitch((float) -(Math.toRadians(mc.player.rotationPitch))).rotateYaw((float) -Math.toRadians(mc.player.rotationYaw));
               GL11.glVertex3d(vec.xCoord, vec.yCoord + mc.player.getEyeHeight(), vec.zCoord);
               GL11.glVertex3d(x, y, z);
               GL11.glEnd();
               AntiAliasingUtility.unhook(true, false, false);
               GL11.glEnable(GL11.GL_TEXTURE_2D);
               GL11.glEnable(GL11.GL_DEPTH_TEST);
               GL11.glDepthMask(true);
               GL11.glDisable(GL11.GL_BLEND);
               GL11.glPopMatrix();
               GlStateManager.resetColor();
            }
         }
         GL11.glDisable(3042);
         GL11.glDepthMask(true);
         GL11.glDisable(GL11.GL_LINE_SMOOTH);
         GL11.glEnable(3553);
         GL11.glEnable(2929);
         GL11.glDisable(2848);
         GL11.glPopMatrix();
         RenderUtility.glColor(Color.white.getRGB());
      }
   }

   @EventTarget
   public void render2D(EventRender2D event) {
      if (tracerMode.is("�������")) {
         for (Ex ex : exes) {
            if (ex.use) {
               GlStateManager.pushMatrix();
               double yaw = Math.toDegrees(Math.atan2(ex.pos.zCoord - mc.player.posZ, ex.pos.xCoord - mc.player.posX)) - mc.player.rotationYaw - 90;
               GL11.glTranslated((new ScaledResolution(mc)).getScaledWidth() / 2d + 1d, (new ScaledResolution(mc)).getScaledHeight() / 2d, 0);
               GL11.glTranslated((Math.cos(Math.toRadians(yaw - 90)) * 20), Math.sin(Math.toRadians(yaw - 90)) * 20, 0);
               GL11.glRotated(yaw, 0, 0, 1);
               RenderUtility.drawImage(new ResourceLocation("nightware/textures/triangle.png"), (int) -9.9f, 0, (int) 17.5f, (int) 17.5f, -1);
               GlStateManager.popMatrix();
            }
         }
      }

      if (!mc.gameSettings.showDebugInfo) {
         int color = NightWare.getInstance().getC(0).getRGB();
         int color2 = NightWare.getInstance().getC(500).getRGB();
         boolean isDark = NightWare.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());

         float x = this.siDraggable.getX();
         float y = this.siDraggable.getY();
         if (chouse > exes.size() - 1) {
            if (exes.size() > 0) {
               chouse = exes.size() - 1;
            } else {
               chouse = -1;
            }
         }
         for (int i = 0; i < exes.size(); i++) {
            if (exes.get(i).anim.getAnim() == 0 && exes.get(i).anim.to == 0) {
               exes.remove(i);
            }
         }
         /*if (exes.size() >= 20) {
            exes.clear();
         }*/
         this.siDraggable.setHeight(15 + exes.size() * 16 + 4);
         animSize.to = this.siDraggable.getHeight();
         int bgColor = isDark ? new Color(30, 30, 30, 230).getRGB() : new Color(255, 255, 255, 220).getRGB();
         RenderUtility.drawGradientGlow(x, y, this.siDraggable.getWidth(), this.siDraggable.getHeight(), 10, color, color2, color, color2);
         RenderUtility.Cornered(x, y, this.siDraggable.getWidth(), this.siDraggable.getHeight(), 5, 5, 5, 5, bgColor);
         y += 3;
         Fonts.mntsb16.drawGradientCenteredString("��������� �����", x + this.siDraggable.getWidth() / 2 + 0, y + 15 / 2 - Fonts.mntsb16.getFontHeight() / 2, NightWare.getInstance().getC(0), NightWare.getInstance().getC(500));
         animChouse.to = chouse * 16;
         float i = 15;
         for (Ex ex : exes) {
            ex.render(x, i + y);
            i += ex.getFontHeight();
         }
         if (chouse != -1) {
            int middleColor = ColorUtility.interpolateColorC(color, color2, 0.5F).getRGB();
            RenderUtility.drawRect(x, y + 10 + animChouse.getAnim() + 16 / 2, 1, 12, middleColor);
            RenderUtility.drawRect(x + this.siDraggable.getWidth() - 1, y + 10 + animChouse.getAnim() + 16 / 2, 1, 12, middleColor);
         }
      }
   }

   public class Ex {
      Vec3d pos;
      String id;
      int count = 1;
      long time;
      boolean discontinuous = false;
      boolean use = false;
      AnimationUtils anim;

      public Ex(Vec3d pos, String id) {
         anim = new AnimationUtils(0, 1, 0.05f);
         this.pos = pos;
         this.id = id;
         time = System.currentTimeMillis();
      }

      public void render(float x, float y) {
         int col = new Color(35, 35, 35).getRGB();
         int color = NightWare.getInstance().getC(0).getRGB();
         int color2 = NightWare.getInstance().getC(500).getRGB();
         int middleColor = ColorUtility.interpolateColorC(color, color2, 0.5F).getRGB();
         boolean isDark = NightWare.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
         GL11.glPushMatrix();

         ItemStack itemStack = new ItemStack(Items.DIAMOND_SHOVEL);
         String name = id;
         if (id.contains("shoot") || id.contains("separate")) {
            int dam = 0;
            if (id.contains("bow") && !id.contains("prepare")) {
               dam = 720;
               name = "���";
            } else if (id.contains("semi_auto_rifle")) {
               dam = 713;
               name = "��������";
            } else if (id.contains("smg")) {
               dam = 710;
               name = "SMG";
            } else if (id.contains("thompson")) {
               dam = 714;
               name = "�����";
            } else if (id.contains("python")) {
               dam = 716;
               name = "�����";
            } else if (id.contains("assault_rifle")) {
               dam = 702;
               name = "�����";
            } else if (id.contains("double")) {
               dam = 722;
               name = "������";
            } else if (id.contains("spas12")) {
               dam = 707;
               name = "Spas-12";
            } else if (id.contains("mp5")) {
               dam = 709;
               name = "MP5";
            } else if (id.contains("pump")) {
               dam = 717;
               name = "����";
            } else if (id.contains("pipe")) {
               dam = 700;
               name = "����";
            } else if (id.contains("bolt")) {
               dam = 703;
               name = "����";
            } else if (id.contains("revolver")) {
               dam = 711;
               name = "�����";
            } else if (id.contains("hmlmg")) {
               dam = 724;
               name = "HMLMG";
            } else if (id.contains("grenade_launcher") || id.contains("start")) {
               dam = 725;
               name = "���������";
            } else if (id.contains("semi_auto_pistol")) {
               dam = 701;
               name = "�����";
            } else if (id.contains("m39")) {
               dam = 719;
               name = "M39";
            } else if (id.contains("prototype")) {
               dam = 727;
               name = "����-17";
            } else if (id.contains("l96")) {
               dam = 715;
               name = "L96";
            } else if (id.contains("nailgun")) {
               dam = 712;
               name = "�������";
            } else if (id.contains("m249")) {
               dam = 724;
               name = "M249";
            } else if (id.contains("lr300")) {
               dam = 706;
               name = "LR-300";
            } else if (id.contains("m92")) {
               dam = 705;
               name = "M92";
            } else if (id.contains("eoka")) {
               dam = 704;
               name = "Eoka";
            } else if (id.contains("speargun") || id.contains("guntrap")) {
               dam = 16;
               name = "Guntrap";
            }

            itemStack.itemDamage = dam;

         } else if (id.contains("explosion")) {

            if (id.contains("explosive_rifle_bullet")) {
               itemStack = new ItemStack(Items.field_190930_cZ);
               name = "Explosive";
            }
            if (id.contains("satchel_charge")) {
               itemStack = new ItemStack(Items.FLINT);
               name = "Satchel Charge";
            }
            if (id.contains("grenade_f1")) {
               itemStack = new ItemStack(Items.BOOK);
               name = "F1 Grenade";
            }
            if (id.contains("beancan_grenade")) {
               itemStack = new ItemStack(Items.SPECKLED_MELON);
               name = "Beancan Grenade";
            }
            if (id.contains("timed_explosive_charge")) {
               itemStack = new ItemStack(Items.SLIME_BALL);
               name = "C4";
            }
            if (id.contains("rocket")) {
               itemStack = new ItemStack(Items.PRISMARINE_SHARD);
               name = "Rocket";
            }
            if (id.contains("grenade_40mm_he")) {
               itemStack = new ItemStack(Items.DIAMOND_PICKAXE);
               name = "HE Grenade";
               itemStack.itemDamage = 725;
            }
         }


         RenderUtility.renderItem(itemStack, (int) (x + 1 - 100 + anim.getAnim() * 100), (int) y);
         if (id.contains("explosive_rifle_bullet")) {
            GL11.glPushMatrix();
            RenderUtility.customScaledObject2D((int) (x + 1 - 100 + anim.getAnim() * 100) + 1 + 8, (int) y + 1 + 8, 16, 16, 0.5f);
            RenderUtility.renderItem(new ItemStack(Items.field_190930_cZ), (int) (x + 1 - 100 + anim.getAnim() * 100), (int) y);
            GL11.glPopMatrix();
         }
         Fonts.mntssb14.drawString(name, x + 20 - 100 + anim.getAnim() * 100 + 0.5f, y + 18 / 2 - Fonts.mntssb14.getFontHeight() / 2 + 0.7f, use ? color : isDark ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
         String text = Math.round(mc.player.getDistance(pos.xCoord, pos.yCoord, pos.zCoord)) + "m";
         if (count > 1) {
            text += " x" + count;
         }
         Fonts.mntssb14.drawString(text, x + 22 + anim.getAnim() * 100 + -Fonts.mntssb14.getStringWidth(text + " "), y + 18 / 2 - Fonts.mntssb14.getFontHeight() / 2 + 0.7f, use ? color : isDark ? Color.WHITE.getRGB() : Color.BLACK.getRGB());

         GL11.glPopMatrix();
      }

      public void remove() {
         anim.to = 0;
      }

      public float getFontHeight() {
         return 16 * anim.getAnim();
      }
   }
}