package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import com.google.common.collect.Lists;
import com.mojang.realmsclient.gui.ChatFormatting;
import nightware.main.NightWare;
import nightware.main.event.render.EventRender2D;
import nightware.main.manager.dragging.DragManager;
import nightware.main.manager.dragging.Draggable;
import nightware.main.manager.theme.Themes;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.impl.combat.AimBot;
import nightware.main.module.setting.impl.MultiBooleanSetting;
import nightware.main.utility.misc.FontAnim;
import nightware.main.utility.render.animation.Animation;
import nightware.main.utility.render.animation.Direction;
import nightware.main.utility.render.animation.impl.DecelerateAnimation;
import nightware.main.utility.render.font.Fonts;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.*;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import nightware.main.utility.render.ColorUtility;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.StencilUtility;

import static nightware.main.module.Utils.mc;

@ModuleAnnotation(
        name = "Hud",
        category = Category.RENDER
)
public class Hud extends Module {
   public static final MultiBooleanSetting elements = new MultiBooleanSetting("Элементы", Arrays.asList("Логотип", "Координаты", "Инвентарь", "Броня", "Таргет худ"));
   private final String username = NightWare.getInstance().getUserInfo().getName();
   private final Draggable invViewDraggable = DragManager.create(this, "Inventory View", 10, 100);
   private final Draggable potionsDraggable = DragManager.create(this, "Potions", 10, 300);
   private final Draggable targetHudDraggable = DragManager.create(this, "Target HUD", 300, 10);
   public float realOffset = 0.0F;
   private EntityLivingBase currentTarget = null;
   private final Animation animation;
   public static FontAnim wtA = new FontAnim(1500, Arrays.asList("", NightWare.getInstance().getUserInfo().getName() + " [UID: " + NightWare.getInstance().getUserInfo().getUid() + "]", "Дата окончания вашей подписки: " + NightWare.till, "https://dsc.gg/nightwareofc", "https://vk.com/nightwareofc"));
   private double hp;
   public Hud() {
      this.animation = new DecelerateAnimation(175, 1.0F, Direction.BACKWARDS);
      this.potionsDraggable.setWidth(105.0F);
      this.invViewDraggable.setWidth(170.0F);
      this.invViewDraggable.setHeight(72.0F);
      this.targetHudDraggable.setWidth(112.0F);
      this.targetHudDraggable.setHeight(38.0F);
   }

   @EventTarget
   public void onRender2D(EventRender2D event) {
      if (!mc.gameSettings.showDebugInfo) {
         int itemOffset;
         int scaledHeight = event.getResolution().getScaledHeight();
         int color = NightWare.getInstance().getC(0).getRGB();
         int color2 = NightWare.getInstance().getC(500).getRGB();
         boolean isDark = NightWare.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
         int textColor = isDark ? new Color(255, 255, 255).getRGB() : new Color(44, 44, 44).getRGB();
         int bgColor = isDark ? new Color(30, 30, 30, 230).getRGB() : new Color(255, 255, 255, 220).getRGB();
         GlStateManager.pushMatrix();

         if (elements.get(0)) {
            String text = "NightWare " + ChatFormatting.BOLD + "| " + ChatFormatting.RESET + Minecraft.getDebugFPS() + "fps " + ChatFormatting.BOLD + "| " + ChatFormatting.RESET + (mc.getConnection().getPlayerInfo(mc.player.getUniqueID()).getResponseTime()) + "ms";
            String textA = wtA.done;
            int width = Math.max(Fonts.mntsb16.getStringWidth(text),Fonts.mntssb14.getStringWidth(textA)) + 14;
            RenderUtility.drawGradientGlow(6, 6, width, 20, 5, color, color2, color, color2);
            RenderUtility.drawRoundedRect(6, 6, width, 20, 5, bgColor);
            RenderUtility.drawRoundedGradientRect(8, 7.5f, 6, 17, 5, 1, color, color2, color, color2);
            RenderUtility.drawGradientGlow(8, 7.5f, 6, 17, 5, color, color2, color, color2);
            Fonts.mntsb16.drawString(text, 16, 9, textColor);
            Fonts.mntssb14.drawString(textA, 16, 19, textColor);
         }

         if (elements.get(1)) {
            Fonts.mntsb16.drawGradientString("X: " + mc.player.getPosition().getX() + ", Y: " + mc.player.getPosition().getY() + ", Z: " + mc.player.getPosition().getZ(), 2, scaledHeight - 8, NightWare.getInstance().getC(0), NightWare.getInstance().getC(250));
         }

         if (elements.get(2)) {
            RenderUtility.drawGradientGlow((float)this.invViewDraggable.getX(), (float)this.invViewDraggable.getY(), 170.0F, 72.0F, 10, color,color2, color, color2);
            RenderUtility.drawRoundedRect((float)this.invViewDraggable.getX(), (float)this.invViewDraggable.getY(), 170.0F, 72.0F, 5.0F, bgColor);
            Fonts.nunitoBold16.drawGradientCenteredString("Инвентарь", (float)(this.invViewDraggable.getX() + (170 / 2)), (float)this.invViewDraggable.getY() + 4F, NightWare.getInstance().getC(0), NightWare.getInstance().getC(500));

            for(itemOffset = 9; itemOffset < 36; ++itemOffset) {
               ItemStack itemStack = mc.player.inventory.getStackInSlot(itemOffset);
               if (!itemStack.isEmpty()) {
                  int index = itemOffset - 9;
                  RenderUtility.drawItemStack(itemStack, this.invViewDraggable.getX() + 5 + index % 9 * 18, this.invViewDraggable.getY() + 16 + index / 9 * 18);
               }
            }
         }

         Iterator var17;

         if (elements.get(4)) {
            if (AimBot.target != null) {
               this.currentTarget = AimBot.target.getTarget();
            } else if (mc.currentScreen instanceof GuiChat) {
               this.currentTarget = mc.player;
            }

            if (this.currentTarget != null) {
               if (AimBot.target == null && !(mc.currentScreen instanceof GuiChat)) {
                  this.animation.setDirection(Direction.BACKWARDS);
               } else {
                  this.animation.setDirection(Direction.FORWARDS);
               }

               RenderUtility.scaleStart((float)(this.targetHudDraggable.getX() + 56), (float)(this.targetHudDraggable.getY() + 19), this.animation.getOutput());
               RenderUtility.drawGradientGlow((float)this.targetHudDraggable.getX(), (float)this.targetHudDraggable.getY(), 112.0F, 38.0F, 10, isDark ? new Color(30, 30, 30, 180) : new Color(255, 255, 255, 220), isDark ? new Color(30, 30, 30, 180) : new Color(255, 255, 255, 220), isDark ? new Color(30, 30, 30, 180) : new Color(255, 255, 255, 220), isDark ? new Color(30, 30, 30, 180) : new Color(255, 255, 255, 220));
               RenderUtility.drawRoundedRect((float)this.targetHudDraggable.getX(), (float)this.targetHudDraggable.getY(), 112.0F, 38.0F, 5.0F, isDark ? new Color(30, 30, 30, 180).getRGB() : new Color(255, 255, 255, 220).getRGB());
               if (this.currentTarget instanceof EntityPlayer) {
                  StencilUtility.initStencilToWrite();
                  RenderUtility.drawRoundedRect((float)(this.targetHudDraggable.getX() + 4), (float)(this.targetHudDraggable.getY() + 4), 30.0F, 30.0F, 5.0F, -1);
                  StencilUtility.readStencilBuffer(1);
                  float hurtPercent = getHurtPercent(this.currentTarget);
                  GlStateManager.pushMatrix();
                  GlStateManager.color(1.0F, 1.0F - hurtPercent, 1.0F - hurtPercent, 1.0F);
                  mc.getTextureManager().bindTexture(((AbstractClientPlayer)this.currentTarget).getLocationSkin());
                  GlStateManager.enableTexture2D();
                  Gui.drawScaledCustomSizeModalRect(this.targetHudDraggable.getX() + 4, this.targetHudDraggable.getY() + 4, 8.0F, 8.0F, 8, 8, 30, 30, 64.0F, 64.0F);
                  GlStateManager.popMatrix();
                  StencilUtility.uninitStencilBuffer();
               } else {
                  Fonts.tenacityBold28.drawCenteredString("?", (float)(this.targetHudDraggable.getX() + 19), (float)(this.targetHudDraggable.getY() + 19) - (float)Fonts.tenacityBold28.getFontHeight() / 2.0F, isDark ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
               }

               StencilUtility.initStencilToWrite();
               RenderUtility.drawGradientGlow((float)this.targetHudDraggable.getX(), (float)this.targetHudDraggable.getY(), 112.0F, 38.0F, 10, isDark ? new Color(30, 30, 30, 180) : new Color(255, 255, 255, 220), isDark ? new Color(30, 30, 30, 180) : new Color(255, 255, 255, 220), isDark ? new Color(30, 30, 30, 180) : new Color(255, 255, 255, 220), isDark ? new Color(30, 30, 30, 180) : new Color(255, 255, 255, 220));
               RenderUtility.drawRoundedRect((float)this.targetHudDraggable.getX(), (float)this.targetHudDraggable.getY(), 112.0F, 38.0F, 5.0F, isDark ? new Color(30, 30, 30, 180).getRGB() : new Color(255, 255, 255, 220).getRGB());
               StencilUtility.readStencilBuffer(1);
               Fonts.tenacityBold18.drawSubstring(TextFormatting.getTextWithoutFormattingCodes(this.currentTarget.getName()), (float)(this.targetHudDraggable.getX() + 38), (float)(this.targetHudDraggable.getY() + 4), isDark ? Color.WHITE.getRGB() : Color.BLACK.getRGB(), 56.0F);
               StencilUtility.uninitStencilBuffer();
               itemOffset = 38;

               for(var17 = this.currentTarget.getEquipmentAndArmor().iterator(); var17.hasNext(); itemOffset += 12) {
                  ItemStack itemStack = (ItemStack)var17.next();
                  RenderUtility.drawRoundedRect((float)(this.targetHudDraggable.getX() + itemOffset), (float)(this.targetHudDraggable.getY() + 14), 10.0F, 10.0F, 4.0F, isDark ? new Color(22, 22, 22).getRGB() : new Color(175, 175, 175).getRGB());
                  if (!itemStack.isEmpty()) {
                     GlStateManager.pushMatrix();
                     GlStateManager.translate((float)(this.targetHudDraggable.getX() + itemOffset + 1), (float)(this.targetHudDraggable.getY() + 15), 0.0F);
                     GlStateManager.scale(0.5D, 0.5D, 0.5D);
                     RenderUtility.drawItemStack(itemStack, 0, 0);
                     GlStateManager.popMatrix();
                  }
               }

               this.hp = mc.player.getDistanceToEntity(currentTarget);
               String hpt = (hp + "").replace(".", "KKSAL").replaceAll("KKSAL.*", "");
               Fonts.mntssb14.drawCenteredString(hpt + " блоков", (float)(this.targetHudDraggable.getX() + 73), (float)(this.targetHudDraggable.getY() + 29), isDark ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
               RenderUtility.scaleEnd();
            }
         }

         GlStateManager.popMatrix();
         if (elements.get(3)) {
            List<ItemStack> armor = Lists.reverse(mc.player.inventory.armorInventory);

            for(int i = 0; i < 4; ++i) {
               RenderUtility.drawItemStack((ItemStack)armor.get(i), event.getResolution().getScaledWidth() - 75 + (i * 18), event.getResolution().getScaledHeight() - 19);
            }
         }
      }
   }


   public static String getPotionPower(PotionEffect potionEffect) {
      if (potionEffect.getAmplifier() == 1) {
         return "II";
      } else if (potionEffect.getAmplifier() == 2) {
         return "III";
      } else if (potionEffect.getAmplifier() == 3) {
         return "IV";
      } else if (potionEffect.getAmplifier() == 4) {
         return "V";
      } else if (potionEffect.getAmplifier() == 5) {
         return "VI";
      } else if (potionEffect.getAmplifier() == 6) {
         return "VII";
      } else if (potionEffect.getAmplifier() == 7) {
         return "VIII";
      } else if (potionEffect.getAmplifier() == 8) {
         return "IX";
      } else {
         return potionEffect.getAmplifier() == 9 ? "X" : "";
      }
   }

   public static String getDuration(PotionEffect potionEffect) {
      return potionEffect.getIsPotionDurationMax() ? "**:**" : StringUtils.ticksToElapsedTime(potionEffect.getDuration());
   }

   private static float getHurtPercent(EntityLivingBase entity) {
      return ((float)entity.hurtTime - (entity.hurtTime != 0 ? mc.timer.field_194148_c : 0.0F)) / 10.0F;
   }
}
