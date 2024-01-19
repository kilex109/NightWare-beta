package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.renderer.GlStateManager;
import nightware.main.NightWare;
import nightware.main.event.render.EventRender2D;
import nightware.main.manager.theme.Themes;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.BooleanSetting;
import nightware.main.module.setting.impl.ModeSetting;
import nightware.main.module.setting.impl.NumberSetting;
import nightware.main.utility.render.ColorUtility;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.animation.Animation;
import nightware.main.utility.render.animation.Direction;
import nightware.main.utility.render.font.FontRenderer;
import nightware.main.utility.render.font.Fonts;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@ModuleAnnotation(
   name = "Arraylist",
   category = Category.RENDER
)
public class Arraylist extends Module {
   public BooleanSetting hideRender = new BooleanSetting("Hide Render", false);
   public BooleanSetting shadow = new BooleanSetting("Shadow", true);
   public static NumberSetting colorSpeed = new NumberSetting("Color Speed", 6.0F, 1.0F, 9.0F, 1.0F);
   public BooleanSetting lowerCase = new BooleanSetting("Lower Case", false);

   @EventTarget
   public void onRender2D(EventRender2D event) {
      if (!mc.gameSettings.showDebugInfo) {
         float x = 9.0F;
         float y = 54.0F;
         int offset = 0;
         int count = 0;
         boolean isDark = NightWare.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
         GlStateManager.pushMatrix();
         List<Module> sortedModules = this.getSortedModules();
         Iterator var8;
         Module module;
         String moduleName;
         if (this.shadow.get()) {
            var8 = sortedModules.iterator();

            label144:
            while(true) {
               Animation moduleAnimation;
               do {
                  do {
                     if (!var8.hasNext()) {
                        break label144;
                     }

                     module = (Module)var8.next();
                  } while(this.hideRender.get() && module.getCategory() == Category.RENDER);

                  moduleName = this.lowerCase.get() ? module.getName().toLowerCase() : module.getName();
                  moduleAnimation = module.getAnimation();
               } while(!module.isEnabled() && moduleAnimation.finished(Direction.BACKWARDS));

               if (!moduleAnimation.isDone()) {
                  RenderUtility.scaleStart(x - 1.0F + (float)(this.getFont().getStringWidth(moduleName) + 8 + (this.getFont().equals(Fonts.minecraft13) ? 1 : 0)) / 2.0F, y + (float)offset - 1.0F + 6.5F, moduleAnimation.getOutput());
               }

               float alphaAnimation = moduleAnimation.getOutput();
               if (!moduleAnimation.isDone()) {
                  RenderUtility.scaleEnd();
               }

               offset = (int)((float)offset + moduleAnimation.getOutput() * 10.0F);
               ++count;
            }
         }

         offset = 0;
         count = 0;
         var8 = sortedModules.iterator();

         while(true) {
            float alphaAnimation;
            Animation moduleAnimation;
            do {
               do {
                  if (!var8.hasNext()) {
                     offset = 0;
                     count = 0;
                     var8 = sortedModules.iterator();

                     while(true) {
                        do {
                           do {
                              if (!var8.hasNext()) {
                                 GlStateManager.popMatrix();
                                 return;
                              }

                              module = (Module)var8.next();
                           } while(this.hideRender.get() && module.getCategory() == Category.RENDER);

                           moduleAnimation = module.getAnimation();
                           moduleAnimation.setDirection(module.isEnabled() ? Direction.FORWARDS : Direction.BACKWARDS);
                        } while(!module.isEnabled() && moduleAnimation.finished(Direction.BACKWARDS));

                        alphaAnimation = moduleAnimation.getOutput();
                        RenderUtility.drawRect(x, y + (float)offset, 2.0F, 11.0F, ColorUtility.applyOpacity(getArrayColor(count), alphaAnimation).getRGB());
                        RenderUtility.drawGradientGlow(x, y + (float)offset, 2.0F, 11.0F, 3, ColorUtility.applyOpacity(getArrayColor(count), alphaAnimation).getRGB(), ColorUtility.applyOpacity(getArrayColor(count), alphaAnimation).getRGB(), ColorUtility.applyOpacity(getArrayColor(count), alphaAnimation).getRGB(), ColorUtility.applyOpacity(getArrayColor(count), alphaAnimation).getRGB());
                        offset = (int)((float)offset + moduleAnimation.getOutput() * 10.0F);
                        ++count;
                     }
                  }

                  module = (Module)var8.next();
               } while(this.hideRender.get() && module.getCategory() == Category.RENDER);

               moduleName = this.lowerCase.get() ? module.getName().toLowerCase() : module.getName();
               alphaAnimation = (float)this.getFont().getStringWidth(moduleName) + 4.5F + (float)(this.getFont().equals(Fonts.minecraft13) ? 1 : 0);
               moduleAnimation = module.getAnimation();
               moduleAnimation.setDirection(module.isEnabled() ? Direction.FORWARDS : Direction.BACKWARDS);
            } while(!module.isEnabled() && moduleAnimation.finished(Direction.BACKWARDS));

            if (!moduleAnimation.isDone()) {
               RenderUtility.scaleStart(x + 2.0F + alphaAnimation / 2.0F, y + (float)offset + 5.5F, moduleAnimation.getOutput());
            }

            alphaAnimation = moduleAnimation.getOutput();
            RenderUtility.drawRect(x, y + (float)offset, this.getFont().getStringWidth(moduleName) + 6, 11.0F, isDark ? (new Color(30, 30, 30)).getRGB() : (new Color(255, 255, 255)).getRGB());
            this.getFont().drawString(moduleName, x + 4.0F + (float)(this.getFont().equals(Fonts.minecraft13) ? 1 : 0), y + (float)offset + (11.0F - (float)this.getFont().getStringHeight(moduleName)) / 2.0F + 1.0F, isDark ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
            if (!moduleAnimation.isDone()) {
               RenderUtility.scaleEnd();
            }

            offset = (int)((float)offset + moduleAnimation.getOutput() * 10.0F);
            ++count;
         }
      }
   }

   public List<Module> getSortedModules() {
      return (List) NightWare.getInstance().getModuleManager().getModules().stream().sorted((module1, module2) -> {
         float width1 = (float)this.getFont().getStringWidth(this.lowerCase.get() ? module1.getName().toLowerCase() : module1.getName());
         float width2 = (float)this.getFont().getStringWidth(this.lowerCase.get() ? module2.getName().toLowerCase() : module2.getName());
         return Float.compare(width2, width1);
      }).collect(Collectors.toList());
   }

   public FontRenderer getFont() {
      FontRenderer font = Fonts.mntssb16;
      return font;
   }

   public static Color getColor(int index) {
      Color color = NightWare.getInstance().getC(0);
      Color color2 = NightWare.getInstance().getC(500);
      return ColorUtility.gradient((int)(10.0F - colorSpeed.get()), index * 2, color, color2);
   }

   public static Color getArrayColor(int index) {
      Color color = NightWare.getInstance().getC(0);
      Color color2 = NightWare.getInstance().getC(500);
      return ColorUtility.gradient((int)(10.0F - colorSpeed.get()), index * 20, color, color2);
   }
}
