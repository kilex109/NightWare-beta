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
import nightware.main.utility.math.MathUtility;
import nightware.main.utility.misc.FontAnim;
import nightware.main.utility.render.SmartScissor;
import nightware.main.utility.render.animation.Animation;
import nightware.main.utility.render.animation.AnimationMath;
import nightware.main.utility.render.animation.Direction;
import nightware.main.utility.render.animation.impl.DecelerateAnimation;
import nightware.main.utility.render.blur.BlurUtility;
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
import org.lwjgl.opengl.GL11;

@ModuleAnnotation(
        name = "Hud",
        category = Category.RENDER
)
public class Hud extends Module {
   public static final MultiBooleanSetting elements = new MultiBooleanSetting("Elements", Arrays.asList("Watermark", "Server Info", "Coords", "Inventory", "Armor", "Target HUD"));
   private final String username = NightWare.getInstance().getUserInfo().getName();
   private final Draggable invViewDraggable = DragManager.create(this, "Inventory View", 10, 100);
   private final Draggable potionsDraggable = DragManager.create(this, "Potions", 10, 300);
   private final Draggable targetHudDraggable = DragManager.create(this, "Target HUD", 300, 10);
   public float realOffset = 0.0F;
   private EntityLivingBase currentTarget = null;
   private final Animation animation;
   public static FontAnim wtA = new FontAnim(1500, Arrays.asList("", NightWare.getInstance().getUserInfo().getName() + " [UID: " + NightWare.getInstance().getUserInfo().getUid() + "]", "Ваше время: " + (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(new Date()), "https://dsc.gg/nightwareofc", "https://vk.com/nightwareofc"));
   private float hp;
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
         int scaledHeight = event.getResolution().getScaledHeight();
         int color = NightWare.getInstance().getC(0).getRGB();
         int color2 = NightWare.getInstance().getC(500).getRGB();
         int middleColor = ColorUtility.interpolateColorC(color, color2, 0.5F).getRGB();
         boolean isDark = NightWare.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
         GlStateManager.pushMatrix();
         int itemOffset;
         String date;
         String userinfo;
         String doneText;
         String bpsText;
         String serverIP;
         String serverPing;
         String serverData;

         if (elements.get(0)) {
            date = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(new Date()) + NightWare.clientPrefixC() + " // " + ChatFormatting.RESET + Minecraft.getDebugFPS() + "fps";
            userinfo = NightWare.getInstance().getUserInfo().getName() + " - " + NightWare.getInstance().getUserInfo().getUid();
            doneText = NightWare.clientPrefixC() + "NightWare" + ChatFormatting.RESET + " -> " + date + NightWare.clientPrefixC() + " // " + ChatFormatting.RESET + userinfo;
            NetworkPlayerInfo playerInfo = mc.getConnection().getPlayerInfo(mc.player.getGameProfile().getId());
            serverIP = (!mc.isSingleplayer() ? mc.getCurrentServerData().serverIP.toLowerCase() : "localhost").replace("192.168.0.2", "play.rustme.net");
            serverPing = (playerInfo != null && !mc.isSingleplayer() ? playerInfo.getResponseTime() : 0) + " ms";
            serverData = serverIP + NightWare.clientPrefixC() + " // " + ChatFormatting.RESET + serverPing;

            RenderUtility.drawGradientGlow(10, 10.0F, 10 + Fonts.nunitoBold16.getStringWidth(doneText), 15.0F, 10, isDark ? new Color(30, 30, 30, 180) : new Color(255, 255, 255, 220), isDark ? new Color(30, 30, 30, 180) : new Color(255, 255, 255, 220), isDark ? new Color(30, 30, 30, 180) : new Color(255, 255, 255, 220), isDark ? new Color(30, 30, 30, 180) : new Color(255, 255, 255, 220));
            RenderUtility.drawGradientGlow(10.0F, 24F, 10 + Fonts.nunitoBold16.getStringWidth(serverData), 15.0F, 10, isDark ? new Color(30, 30, 30, 180) : new Color(255, 255, 255, 220), isDark ? new Color(30, 30, 30, 180) : new Color(255, 255, 255, 220), isDark ? new Color(30, 30, 30, 180) : new Color(255, 255, 255, 220), isDark ? new Color(30, 30, 30, 180) : new Color(255, 255, 255, 220));

            RenderUtility.Cornered(10, 10.0F, 10 + Fonts.nunitoBold16.getStringWidth(doneText), 15.0F, 5, 0, 5, 5, isDark ? new Color(30, 30, 30, 180).getRGB() : new Color(255, 255, 255, 220).getRGB());
            RenderUtility.Cornered(10.0F, 24.749F, 10 + Fonts.nunitoBold16.getStringWidth(serverData), 15.0F, 0, 5, 0, 5, isDark ? new Color(30, 30, 30, 180).getRGB() : new Color(255, 255, 255, 220).getRGB());

            Fonts.nunitoBold16.drawString(doneText, 15, 15.0F, isDark ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
            Fonts.nunitoBold16.drawString(serverData, 15, 29.0F, isDark ? Color.WHITE.getRGB() : Color.BLACK.getRGB());

            /*String text = "NightWare BETA";
            String textA = wtA.done;
            int width = Math.max(Fonts.mntsb16.getStringWidth(text),Fonts.mntssb14.getStringWidth(textA)) + 6;
            RenderUtility.drawGradientGlow(7, 10, width + 2, 22, 6, color, color2, color, color2);
            RenderUtility.drawRoundedGradientRect(8, 10, width, 20, 2, 1, color, color2, color, color2);
            RenderUtility.drawRoundedRect(10, 8, width, 20, 2, new Color(225, 225, 225).getRGB());
            Fonts.mntsb16.drawString(text, 12, 12, new Color(55, 55, 55).getRGB());
            Fonts.mntssb14.drawString(textA, 12, 20, new Color(55, 55, 55).getRGB());*/
         }

         if (elements.get(2)) {
            Fonts.mntsb16.drawGradientString(mc.player.getPosition().getX() + " :: " + mc.player.getPosition().getY() + " :: " + mc.player.getPosition().getZ(), 2, scaledHeight - 8, NightWare.getInstance().getC(0), NightWare.getInstance().getC(250));
         }

         if (elements.get(3)) {
            RenderUtility.drawGradientGlow((float)this.invViewDraggable.getX(), (float)this.invViewDraggable.getY(), 170.0F, 72.0F, 10, isDark ? new Color(30, 30, 30, 180) : new Color(255, 255, 255, 220), isDark ? new Color(30, 30, 30, 180) : new Color(255, 255, 255, 220), isDark ? new Color(30, 30, 30, 180) : new Color(255, 255, 255, 220), isDark ? new Color(30, 30, 30, 180) : new Color(255, 255, 255, 220));
            RenderUtility.drawRoundedRect((float)this.invViewDraggable.getX(), (float)this.invViewDraggable.getY(), 170.0F, 72.0F, 5.0F, isDark ? new Color(30, 30, 30, 180).getRGB() : new Color(255, 255, 255, 220).getRGB());
            Fonts.nunitoBold16.drawGradientCenteredString("Инвентарь", (float)(this.invViewDraggable.getX() + (170 / 2)), (float)this.invViewDraggable.getY() + 4F, NightWare.getInstance().getC(0), NightWare.getInstance().getC(500));
            RenderUtility.drawRect(this.invViewDraggable.getX(), (float)this.invViewDraggable.getY() + 13f, 170.0F, 0.5F, isDark ? new Color(255, 255, 255, 220).getRGB() : (new Color(22, 22, 22)).getRGB());

            for(itemOffset = 9; itemOffset < 36; ++itemOffset) {
               ItemStack itemStack = mc.player.inventory.getStackInSlot(itemOffset);
               if (!itemStack.isEmpty()) {
                  int index = itemOffset - 9;
                  RenderUtility.drawItemStack(itemStack, this.invViewDraggable.getX() + 5 + index % 9 * 18, this.invViewDraggable.getY() + 16 + index / 9 * 18);
               }
            }
         }

         Iterator var17;

         if (elements.get(5)) {
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

               this.hp = MathUtility.clamp(MathUtility.lerp(this.hp, this.currentTarget.getHealth() / this.currentTarget.getMaxHealth(), (float)(12.0D * AnimationMath.deltaTime())), 0.0F, 1.0F);
               RenderUtility.drawRoundedRect((float)(this.targetHudDraggable.getX() + 38), (float)this.targetHudDraggable.getY() + 27.5F, 70.0F, 6.5F, 3.0F, isDark ? (new Color(50, 50, 50)).getRGB() : Color.WHITE.getRGB());
               RenderUtility.drawRoundedGradientRect((float)(this.targetHudDraggable.getX() + 38), (float)this.targetHudDraggable.getY() + 27.5F, 70.0F * this.hp, 6.5F, 3.0F, 1.0F, color, color, color2, color2);
               Fonts.tenacityBold12.drawCenteredString(MathUtility.round((double)(this.hp * 100.0F), 0.10000000149011612D) + "%", (float)(this.targetHudDraggable.getX() + 73), (float)(this.targetHudDraggable.getY() + 29), isDark ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
               RenderUtility.scaleEnd();
            }
         }

         GlStateManager.popMatrix();
         if (elements.get(4)) {
            List<ItemStack> armor = Lists.reverse(mc.player.inventory.armorInventory);

            for(int i = 0; i < 4; ++i) {
               RenderUtility.drawItemStack((ItemStack)armor.get(i), event.getResolution().getScaledWidth() - 75 + (i * 18), event.getResolution().getScaledHeight() - 19);
            }
         }
      }
   }

   private String getCoordsText() {
      if (mc.player == null) {
         return "";
      } else {
         StringBuilder coordsBuilder = (new StringBuilder()).append("XYZ: ");
         if (mc.player.getEntityWorld().provider.getDimensionType().equals(DimensionType.THE_END)) {
            coordsBuilder.append(ChatFormatting.DARK_PURPLE).append(mc.player.getPosition().getX()).append(", ").append(mc.player.getPosition().getY()).append(", ").append(mc.player.getPosition().getZ()).append(ChatFormatting.RESET);
         } else if (mc.player.getEntityWorld().provider.getDimensionType().equals(DimensionType.NETHER)) {
            coordsBuilder.append(ChatFormatting.RED).append(mc.player.getPosition().getX()).append(", ").append(mc.player.getPosition().getY()).append(", ").append(mc.player.getPosition().getZ()).append(ChatFormatting.GREEN).append(" (").append(mc.player.getPosition().getX() * 8).append(", ").append(mc.player.getPosition().getY()).append(", ").append(mc.player.getPosition().getZ() * 8).append(")").append(ChatFormatting.RESET);
         } else {
            coordsBuilder.append(ChatFormatting.GREEN).append(mc.player.getPosition().getX()).append(", ").append(mc.player.getPosition().getY()).append(", ").append(mc.player.getPosition().getZ()).append(ChatFormatting.RED).append(" (").append(mc.player.getPosition().getX() / 8).append(", ").append(mc.player.getPosition().getY()).append(", ").append(mc.player.getPosition().getZ() / 8).append(")").append(ChatFormatting.RESET);
         }

         return coordsBuilder.toString();
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

   private static int getPotionDurationColor(PotionEffect potionEffect, boolean isDark) {
      int potionColor = -1;
      if (potionEffect.getDuration() < 200) {
         potionColor = (new Color(255, 103, 32)).getRGB();
      } else if (potionEffect.getDuration() < 400) {
         potionColor = (new Color(231, 143, 32)).getRGB();
      } else if (potionEffect.getDuration() > 400) {
         potionColor = isDark ? (new Color(205, 205, 205)).getRGB() : (new Color(20, 20, 20)).getRGB();
      }

      return potionColor;
   }

   private int getArmorOffset() {
      int offset = 56;
      if (mc.player.isCreative()) {
         offset -= 15;
      }

      if (!mc.player.isCreative() && mc.player.isInsideOfMaterial(Material.WATER)) {
         offset += 10;
      }

      if (mc.player.getRidingEntity() != null && mc.player.getRidingEntity() instanceof EntityLiving) {
         EntityLiving entity = (EntityLiving)mc.player.getRidingEntity();
         offset = (int)((double)(offset - 10) + Math.ceil((double)((entity.getMaxHealth() - 1.0F) / 20.0F)) * 10.0D) + (mc.player.isCreative() ? 15 : 0);
      }

      return offset;
   }

   public static int getTooltipOffset() {
      int offset = 63;
      if (!mc.player.isCreative() && mc.player.isInsideOfMaterial(Material.WATER)) {
         offset += 10;
      }

      return offset;
   }

   private static float getHurtPercent(EntityLivingBase entity) {
      return ((float)entity.hurtTime - (entity.hurtTime != 0 ? mc.timer.field_194148_c : 0.0F)) / 10.0F;
   }
}
