package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import com.jhlabs.vecmath.Vector4f;
import com.mojang.realmsclient.gui.ChatFormatting;
import nightware.main.NightWare;
import nightware.main.event.render.EventRender2D;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.impl.util.NameProtect;
import nightware.main.utility.render.ColorUtility;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.font.Fonts;
import java.awt.Color;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;

@ModuleAnnotation(
   name = "NameTags",
   category = Category.RENDER
)
public class NameTags extends Module {
   @EventTarget
   public void onRender(EventRender2D event) {
      RenderManager renderMng = mc.getRenderManager();
      EntityRenderer entityRenderer = mc.entityRenderer;
      Iterator var4 = mc.world.playerEntities.iterator();

      while(true) {
         EntityPlayer entity;
         do {
            if (!var4.hasNext()) {
               return;
            }

            entity = (EntityPlayer)var4.next();
         } while(mc.player == entity && mc.gameSettings.thirdPersonView == 0);

         GlStateManager.pushMatrix();
         if (RenderUtility.isInViewFrustum((Entity)entity)) {
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)event.getPartialTicks();
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)event.getPartialTicks();
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)event.getPartialTicks();
            double width = (double)entity.width / 1.5D;
            double height = (double)(entity.height + 0.2F - (entity.isSneaking() ? 0.2F : 0.0F));
            AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
            Vec3d[] vectors = new Vec3d[]{new Vec3d(aabb.minX, aabb.minY, aabb.minZ), new Vec3d(aabb.minX, aabb.maxY, aabb.minZ), new Vec3d(aabb.maxX, aabb.minY, aabb.minZ), new Vec3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vec3d(aabb.minX, aabb.minY, aabb.maxZ), new Vec3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vec3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vec3d(aabb.maxX, aabb.maxY, aabb.maxZ)};
            entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
            Vector4f position = null;
            Vec3d[] var19 = vectors;
            int var20 = vectors.length;

            for(int var21 = 0; var21 < var20; ++var21) {
               Vec3d vector = var19[var21];
               vector = RenderUtility.project2D(2, vector.x - renderMng.viewerPosX, vector.y - renderMng.viewerPosY, vector.z - renderMng.viewerPosZ);
               if (vector != null && vector.z >= 0.0D && vector.z < 1.0D) {
                  if (position == null) {
                     position = new Vector4f((float)vector.x, (float)vector.y, (float)vector.z, 1.0F);
                  }

                  position.x = (float)Math.min(vector.x, (double)position.x);
                  position.y = (float)Math.min(vector.y, (double)position.y);
                  position.z = (float)Math.max(vector.x, (double)position.z);
                  position.w = (float)Math.max(vector.y, (double)position.w);
               }
            }

            if (position != null) {
               mc.entityRenderer.setupOverlayRendering(2);
               double posX = (double)position.x;
               double posY = (double)position.y;
               double endPosX = (double)position.z;
               float center = (float)(posX + (endPosX - posX) / 2.0D);
               String tagName;
               if (NightWare.getInstance().getModuleManager().getModule(NameProtect.class).isEnabled()) {
                  if (NightWare.getInstance().getFriendManager().isFriend(entity.getName())) {
                     tagName = ChatFormatting.GREEN + "[F] " + ChatFormatting.RESET + entity.getDisplayName().getFormattedText().concat("");
                  } else {
                     tagName = entity.getDisplayName().getFormattedText().concat("");
                  }
               } else if (NightWare.getInstance().getFriendManager().isFriend(entity.getName())) {
                  tagName = ChatFormatting.GREEN + "[F] " + ChatFormatting.RESET + entity.getDisplayName().getFormattedText().concat("");
               } else {
                  tagName = entity.getDisplayName().getFormattedText().concat("");
               }

               if (!entity.getUniqueID().equals(UUID.nameUUIDFromBytes(("OfflinePlayer:" + entity.getName()).getBytes(StandardCharsets.UTF_8)))) {
                  tagName = ChatFormatting.GRAY + "[" + ChatFormatting.RED + "BOT" + ChatFormatting.GRAY + "]" + ChatFormatting.RESET + " " + entity.getDisplayName().getFormattedText();
               }

               int bgWidth = Fonts.tenacityBold12.getStringWidth(tagName) + 5;
               RenderUtility.drawRoundedRect(center - (float)bgWidth / 2.0F, (float)posY - 14.5F, (float)bgWidth, (float)(Fonts.tenacityBold12.getFontHeight() + 4), 3.0F, (new Color(30, 30, 30)).getRGB());
               Fonts.tenacityBold12.drawStringWithShadow(tagName, center - (float)Fonts.tenacityBold12.getStringWidth(tagName) / 2.0F, (float)posY - 12.0F, -1);
               List<ItemStack> stacks = new ArrayList(Arrays.asList(entity.getHeldItemMainhand(), entity.getHeldItemOffhand()));
               entity.getArmorInventoryList().forEach(stacks::add);
               stacks.removeIf((w) -> {
                  return w.getItem() instanceof ItemAir;
               });
               int totalSize = stacks.size() * 10;
               int iterable = 0;
               Iterator var31 = stacks.iterator();

               label109:
               while(true) {
                  ItemStack stack;
                  do {
                     if (!var31.hasNext()) {
                        List<String> potions = new ArrayList();
                        Iterator var40 = entity.getActivePotionEffects().iterator();

                        String power;
                        while(var40.hasNext()) {
                           PotionEffect potionEffect = (PotionEffect)var40.next();
                           power = Hud.getPotionPower(potionEffect);
                           ChatFormatting potionColor = null;
                           if (potionEffect.getDuration() < 200) {
                              potionColor = ChatFormatting.RED;
                           } else if (potionEffect.getDuration() < 400) {
                              potionColor = ChatFormatting.GOLD;
                           } else if (potionEffect.getDuration() > 400) {
                              potionColor = ChatFormatting.GREEN;
                           }

                           if (potionEffect.getDuration() != 0) {
                              potions.add(I18n.format(potionEffect.getPotion().getName()) + " " + power + TextFormatting.GRAY + " " + potionColor + Hud.getDuration(potionEffect));
                           }
                        }

                        float startY = 0.0F;

                        for(Iterator var43 = potions.iterator(); var43.hasNext(); startY += 10.0F) {
                           power = (String)var43.next();
                           Fonts.nunito12.drawString(power, endPosX + 5.0D, posY + 10.0D + (double)startY, -1);
                        }
                        break label109;
                     }

                     stack = (ItemStack)var31.next();
                  } while(stack == null);

                  GlStateManager.pushMatrix();
                  GlStateManager.translate((double)(center + (float)(iterable * 20) - (float)totalSize + 2.0F), posY - 35.0D, 0.0D);
                  RenderUtility.drawItemStack(stack, 0, 0);
                  GlStateManager.popMatrix();
                  ++iterable;
                  ArrayList<String> lines = new ArrayList();
                  getEnchantment(lines, stack);
                  int i = 0;

                  for(Iterator var35 = lines.iterator(); var35.hasNext(); ++i) {
                     String s = (String)var35.next();
                     Fonts.nunito12.drawCenteredStringWithShadow(s, center + (float)(iterable * 20) - (float)totalSize - 10.0F, (float)posY - 37.5F - 6.0F - (float)(i * 7), -1);
                  }
               }
            }

            mc.entityRenderer.setupOverlayRendering();
         }

         GlStateManager.popMatrix();
      }
   }

   public static void getEnchantment(ArrayList<String> list, ItemStack stack) {
      Item item = stack.getItem();
      int protection = EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, stack);
      int thorns = EnchantmentHelper.getEnchantmentLevel(Enchantments.THORNS, stack);
      int unbreaking = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);
      int mending = EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack);
      int feather = EnchantmentHelper.getEnchantmentLevel(Enchantments.FEATHER_FALLING, stack);
      int depth = EnchantmentHelper.getEnchantmentLevel(Enchantments.DEPTH_STRIDER, stack);
      int vanishing_curse = EnchantmentHelper.getEnchantmentLevel(Enchantments.field_190940_C, stack);
      int binding_curse = EnchantmentHelper.getEnchantmentLevel(Enchantments.field_190941_k, stack);
      int sweeping = EnchantmentHelper.getEnchantmentLevel(Enchantments.field_191530_r, stack);
      int sharpness = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
      int looting = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack);
      int infinity = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack);
      int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
      int punch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
      int flame = EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack);
      int knockback = EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, stack);
      int fireAspect = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
      int efficiency = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
      int silktouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack);
      int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
      int fireprot = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, stack);
      int blastprot = EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION, stack);
      if (item instanceof ItemAxe) {
         if (sharpness > 0) {
            list.add("Shr" + sharpness);
         }

         if (efficiency > 0) {
            list.add("Eff" + efficiency);
         }

         if (unbreaking > 0) {
            list.add("Unb" + unbreaking);
         }
      }

      if (item instanceof ItemArmor) {
         if (vanishing_curse > 0) {
            list.add("Vanish ");
         }

         if (fireprot > 0) {
            list.add("Firp" + fireprot);
         }

         if (blastprot > 0) {
            list.add("Bla" + blastprot);
         }

         if (binding_curse > 0) {
            list.add("§cBindi ");
         }

         if (depth > 0) {
            list.add("Dep" + depth);
         }

         if (feather > 0) {
            list.add("Fea" + feather);
         }

         if (protection > 0) {
            list.add("Pro" + protection);
         }

         if (thorns > 0) {
            list.add("Thr" + thorns);
         }

         if (mending > 0) {
            list.add("Men ");
         }

         if (unbreaking > 0) {
            list.add("Unb" + unbreaking);
         }
      }

      if (item instanceof ItemBow) {
         if (vanishing_curse > 0) {
            list.add("Vanish ");
         }

         if (binding_curse > 0) {
            list.add("Binding ");
         }

         if (infinity > 0) {
            list.add("Inf" + infinity);
         }

         if (power > 0) {
            list.add("Pow" + power);
         }

         if (punch > 0) {
            list.add("Pun" + punch);
         }

         if (mending > 0) {
            list.add("Men ");
         }

         if (flame > 0) {
            list.add("Fla" + flame);
         }

         if (unbreaking > 0) {
            list.add("Unb" + unbreaking);
         }
      }

      if (item instanceof ItemSword) {
         if (vanishing_curse > 0) {
            list.add("Vanish ");
         }

         if (looting > 0) {
            list.add("Loot" + looting);
         }

         if (binding_curse > 0) {
            list.add("Bindi ");
         }

         if (sweeping > 0) {
            list.add("Swe" + sweeping);
         }

         if (sharpness > 0) {
            list.add("Shr" + sharpness);
         }

         if (knockback > 0) {
            list.add("Kno" + knockback);
         }

         if (fireAspect > 0) {
            list.add("Fir" + fireAspect);
         }

         if (unbreaking > 0) {
            list.add("Unb" + unbreaking);
         }

         if (mending > 0) {
            list.add("Men ");
         }
      }

      if (item instanceof ItemTool) {
         if (unbreaking > 0) {
            list.add("Unb" + unbreaking);
         }

         if (mending > 0) {
            list.add("Men ");
         }

         if (vanishing_curse > 0) {
            list.add("Vanish ");
         }

         if (binding_curse > 0) {
            list.add("Binding ");
         }

         if (efficiency > 0) {
            list.add("Eff" + efficiency);
         }

         if (silktouch > 0) {
            list.add("Sil" + silktouch);
         }

         if (fortune > 0) {
            list.add("For" + fortune);
         }
      }

   }
}
