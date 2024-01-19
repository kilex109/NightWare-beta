package nightware.main.ui.csgui;

import nightware.main.NightWare;
import nightware.main.manager.config.Config;
import nightware.main.manager.config.ConfigManager;
import nightware.main.manager.theme.Theme;
import nightware.main.manager.theme.Themes;
import nightware.main.module.Category;
import nightware.main.module.impl.render.ClickGuiModule;
import nightware.main.ui.csgui.component.Component;
import nightware.main.ui.csgui.component.impl.ConfigComponent;
import nightware.main.ui.csgui.component.impl.ModuleComponent;
import nightware.main.ui.csgui.component.impl.ThemeComponent;
import nightware.main.ui.csgui.window.ColorPickerWindow;
import nightware.main.ui.menu.widgets.SearchField;
import nightware.main.utility.misc.SoundUtility;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.StencilUtility;
import nightware.main.utility.render.animation.Animation;
import nightware.main.utility.render.animation.AnimationMath;
import nightware.main.utility.render.animation.Direction;
import nightware.main.utility.render.animation.impl.DecelerateAnimation;
import nightware.main.utility.render.animation.impl.EaseInOutQuad;
import nightware.main.utility.render.blur.BlurUtility;
import nightware.main.utility.render.font.Fonts;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;

public class CsGui extends GuiScreen {
   public int x;
   public int y;
   public static Category selected;
   public static ColorPickerWindow colorPicker;
   public static List<ModuleComponent> modules;
   public static List<ModuleComponent> modules2;
   public static List<ThemeComponent> guiThemes;
   public static List<ThemeComponent> themes;
   public static List<ConfigComponent> configs;
   public static List<ConfigComponent> configs2;
   private Animation openAnimation;
   private static final Animation moduleAnimation;
   public static SearchField search;
   public static boolean escapeInUse;
   public boolean isClosed;
   public float scrollY = 32.5F;
   private float text1Y = 0.0F;
   private float text2Y = 0.0F;

   public CsGui() {
      ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
      this.x = sr.getScaledWidth() / 2 - this.width / 2 + 90;
      this.y = sr.getScaledHeight() / 2 - this.height / 2;
      search = new SearchField(this.eventButton, Fonts.mntssb16, this.x + 95, this.y + 4, 249, 18);
      NightWare.getInstance().getModuleManager().getModules().forEach((module) -> {
         if (NightWare.getInstance().getModuleManager().getModules().indexOf(module) % 2 == 0) {
            modules.add(new ModuleComponent(module, 120.0F, 30.0F));
         }

         if (NightWare.getInstance().getModuleManager().getModules().indexOf(module) % 2 != 0) {
            modules2.add(new ModuleComponent(module, 120.0F, 30.0F));
         }

      });
      Themes[] var2 = Themes.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Themes theme = var2[var4];
         if (theme.getTheme().getType().equals(Theme.ThemeType.GUI)) {
            guiThemes.add(new ThemeComponent(theme.getTheme(), 55.0F, 30.0F));
         } else {
            themes.add(new ThemeComponent(theme.getTheme(), 55.0F, 30.0F));
         }
      }

   }

   public void initGui() {
      super.initGui();
      this.openAnimation = new EaseInOutQuad(300, 1.0F, Direction.FORWARDS);
      if (colorPicker != null) {
         colorPicker.init();
      }

      updateConfigComponents();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      super.drawScreen(mouseX, mouseY, partialTicks);
      Color[] guiColors = NightWare.getInstance().getThemeManager().getCurrentGuiTheme().getColors();
      boolean isDark = NightWare.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
      if (ClickGuiModule.blur.get()) {
         BlurUtility.drawBlurredScreen(ClickGuiModule.blurRadius.get());
      }

      if (this.isClosed && this.openAnimation.isDone()) {
         this.mc.displayGuiScreen((GuiScreen)null);
         this.isClosed = false;
      }

      float scale = this.openAnimation.getOutput();
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)(this.x + 175), (float)((this.y + 300) / 2), 0.0F);
      GlStateManager.scale(scale, scale, 0.0F);
      GlStateManager.translate((float)(-(this.x + 175)), (float)(-((this.y + 300) / 2)), 0.0F);
      RenderUtility.drawRect((float)this.x, (float)this.y, 20, 20, guiColors[0].getRGB());
      RenderUtility.drawRoundedRect((float)this.x, (float)this.y, 350.0F, 250.0F, 8.0F, 185.0F, 50.0F, guiColors[0].getRGB(), guiColors[1].getRGB());
      Color color = NightWare.getInstance().getC(0);
      Color color2 = NightWare.getInstance().getC(500);

      RenderUtility.Cornered(this.x + 53 - (Fonts.tenacityBold28.getStringWidth(NightWare.NAME + " " + NightWare.BUILD_TYPE) / 2), this.y - 19, Fonts.tenacityBold28.getStringWidth(NightWare.NAME + " " + NightWare.BUILD_TYPE) + 9, 20, 8, 0, 8, 0, 1, guiColors[0].getRGB());
      RenderUtility.drawRect(this.x + 53 - (Fonts.tenacityBold28.getStringWidth(NightWare.NAME + " " + NightWare.BUILD_TYPE) / 2), this.y - 1, Fonts.tenacityBold28.getStringWidth(NightWare.NAME + " " + NightWare.BUILD_TYPE) + 9, 1, guiColors[1].getRGB());
      Fonts.tenacityBold28.drawGradientCenteredString(NightWare.NAME + " " + NightWare.BUILD_TYPE, (float)(this.x + 58), (float)(this.y - 16), NightWare.getInstance().getC(0), NightWare.getInstance().getC(500));

      search.drawTextBox();
      if (search.getText().isEmpty() && !search.isFocused()) {
         Fonts.mntssb16.drawString("����� �������", (float)(this.x + 99), (float)(this.y + 11), (new Color(120, 120, 120)).getRGB());
      }

      Fonts.icons21.drawString("v", (float)(this.x + 339 - Fonts.icons21.getStringWidth("v")), (float)this.y + 10.5F, (new Color(120, 120, 120)).getRGB());
      Fonts.nunitoBold14.drawString("�������", this.x + 4, (float)(this.y + 10), guiColors[3].getRGB());
      Category[] var9 = Category.values();
      int var10 = var9.length;

      for(int var11 = 0; var11 < var10; ++var11) {
         Category category = var9[var11];
         category.getAnimation().setDirection(selected.equals(category) ? Direction.FORWARDS : Direction.BACKWARDS);
         boolean hovered = RenderUtility.isHovered((float)mouseX, (float)mouseY, this.x + 4, (float)(this.y + 20 + 20 * category.ordinal() + 1 + (category.isBottom() ? 15 : 0)), 87.0F, 17.5F);
         if (category.equals(Category.CONFIGS)) {
            Fonts.nunitoBold14.drawString("���������", this.x + 4, (float)(this.y + 25 + 20 * category.ordinal()), guiColors[3].getRGB());
         }

         if (selected.equals(category)) {
            RenderUtility.drawRoundedRect(this.x + 4, (float)(this.y + 20 + 20 * category.ordinal() + 1 + (category.isBottom() ? 15 : 0)), 87.0F * category.getAnimation().getOutput(), 17.5F, 4.0F, guiColors[4].getRGB());
         } else if (hovered) {
            RenderUtility.drawRoundedRect(this.x + 4, (float)(this.y + 20 + 20 * category.ordinal() + 1 + (category.isBottom() ? 15 : 0)), 87.0F, 17.5F, 4.0F, guiColors[5].hashCode());
         } else {
            RenderUtility.drawRoundedRect(this.x + 4, (float)(this.y + 20 + 20 * category.ordinal() + 1 + (category.isBottom() ? 15 : 0)), 87.0F * category.getAnimation().getOutput(), 17.5F, 4.0F, 3);
         }

         Fonts.ace16.drawString(category.getName(), this.x + 26.5F, (float)(this.y + 20 + 20 * category.ordinal()) + 7.5F + (float)(category.isBottom() ? 15 : 0), isDark ? Color.WHITE.getRGB() : (new Color(65, 65, 65)).getRGB());
         Fonts.icons21.drawString(String.valueOf(category.getIcon()), this.x + 9, (float)(this.y + 20 + 20 * category.ordinal()) + 7.5F + (float)(category.isBottom() ? 15 : 0), isDark ? Color.WHITE.getRGB() : (new Color(65, 65, 65)).getRGB());
      }

      RenderUtility.applyRound(0, 0, 12.0F, 1.0F, () -> {
         RenderUtility.drawProfile((float)(this.x + 1500), (float)(this.y + 220), 25.0F, 25.0F);
      });
      Fonts.mntssb16.drawGradientString("User: " + NightWare.getInstance().getUserInfo().getName(), (float)(this.x + 5), (float)(this.y + 210), NightWare.getInstance().getC(0), NightWare.getInstance().getC(250));
      Fonts.mntssb16.drawGradientString("UID: " + NightWare.getInstance().getUserInfo().getUid(), (float)(this.x + 5), (float)(this.y + 220), NightWare.getInstance().getC(0), NightWare.getInstance().getC(250));
      Fonts.mntssb16.drawGradientString("Role: " + NightWare.getInstance().getUserInfo().getRole(), (float)(this.x + 5), (float)(this.y + 230), NightWare.getInstance().getC(0), NightWare.getInstance().getC(250));
      Fonts.mntssb16.drawGradientString("Till: " + NightWare.getInstance().getUserInfo().getTill(), (float)(this.x + 5), (float)(this.y + 240), NightWare.getInstance().getC(0), NightWare.getInstance().getC(250));
      StencilUtility.initStencilToWrite();
      RenderUtility.drawRect((float)this.x + 92.5F, (float)this.y + 26.5F, 257.5F, 223.5F, -1);
      StencilUtility.readStencilBuffer(1);
      if (selected.equals(Category.THEMES)) {
         this.drawThemes(mouseX, mouseY);
      } else if (selected.equals(Category.CONFIGS)) {
         this.drawConfigs(mouseX, mouseY);
      } else {
         this.drawComponents(mouseX, mouseY);
      }

      RenderUtility.drawRect((float)(this.x + 99), (float)this.y + 26.5F, 245.0F, 250.0F, (new Color((float)guiColors[1].getRed() / 255.0F, (float)guiColors[1].getGreen() / 255.0F, (float)guiColors[1].getBlue() / 255.0F, moduleAnimation.getOutput())).getRGB());
      StencilUtility.uninitStencilBuffer();
      if (colorPicker != null) {
         colorPicker.render(mouseX, mouseY);
      }

      GlStateManager.popMatrix();
   }

   private void drawThemes(int mouseX, int mouseY) {
      boolean isDark = NightWare.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
      int xOffset = 0;
      this.scrollY += (float)Mouse.getDWheel() / 10.0F;
      float offset = this.scrollY;
      float sizeOffset1 = 0.0F;
      float sizeOffset2 = 0.0F;
      this.text1Y = AnimationMath.fast(this.text1Y, (float)this.y + offset, 15.0F);
      Fonts.nunitoBold18.drawString("���� �������", (float)(this.x + 100), this.text1Y, isDark ? Color.WHITE.getRGB() : (new Color(65, 65, 65)).getRGB());

      Iterator var8;
      ThemeComponent themeComponent;
      int index;
      for(var8 = guiThemes.iterator(); var8.hasNext(); sizeOffset1 += themeComponent.height + 7.0F) {
         themeComponent = (ThemeComponent)var8.next();
         index = guiThemes.indexOf(themeComponent);
         themeComponent.setX((float)(this.x + 100) + (float)xOffset * (themeComponent.width + 8.0F));
         themeComponent.setY(AnimationMath.fast(themeComponent.y, (float)(this.y + 12) + offset, 15.0F));
         themeComponent.render(mouseX, mouseY);
         ++xOffset;
         if ((index + 1) % 4 == 0) {
            offset += themeComponent.height + 7.0F;
            xOffset = 0;
         }
      }

      this.text2Y = AnimationMath.fast(this.text2Y, (float)(this.y + 49) + offset, 15.0F);
      Fonts.nunitoBold18.drawString("����� ����", (float)(this.x + 100), this.text2Y, isDark ? Color.WHITE.getRGB() : (new Color(65, 65, 65)).getRGB());
      xOffset = 0;
      offset = this.scrollY;

      for(var8 = themes.iterator(); var8.hasNext(); sizeOffset2 += themeComponent.height + 7.0F) {
         themeComponent = (ThemeComponent)var8.next();
         index = themes.indexOf(themeComponent);
         themeComponent.setX((float)(this.x + 100) + (float)xOffset * (themeComponent.width + 8.0F));
         themeComponent.setY(AnimationMath.fast(themeComponent.y, (float)(this.y + 61) + offset, 15.0F));
         themeComponent.render(mouseX, mouseY);
         ++xOffset;
         if ((index + 1) % 4 == 0) {
            offset += themeComponent.height + 7.0F;
            xOffset = 0;
         }
      }

      float scrollMax = Math.max(sizeOffset1, sizeOffset2);
      if (scrollMax > 250.0F) {
         this.scrollY = MathHelper.clamp(this.scrollY, -scrollMax, 32.5F);
      } else {
         this.scrollY = 32.5F;
      }

   }

   private void drawConfigs(int mouseX, int mouseY) {
      this.scrollY += (float)Mouse.getDWheel() / 10.0F;
      float offset = this.scrollY;
      float offset1 = 32.5F;
      float offset2 = 32.5F;

      Iterator var6;
      ConfigComponent configComponent;
      for(var6 = configs.iterator(); var6.hasNext(); offset1 += configComponent.height + 5.0F) {
         configComponent = (ConfigComponent)var6.next();
         configComponent.setX((float)(this.x + 100));
         configComponent.setY(AnimationMath.fast(configComponent.y, (float)this.y + offset, 15.0F));
         configComponent.render(mouseX, mouseY);
         offset += configComponent.height + 5.0F;
      }

      offset = this.scrollY;

      for(var6 = configs2.iterator(); var6.hasNext(); offset2 += configComponent.height + 5.0F) {
         configComponent = (ConfigComponent)var6.next();
         configComponent.setX((float)(this.x + 224));
         configComponent.setY(AnimationMath.fast(configComponent.y, (float)this.y + offset, 15.0F));
         configComponent.render(mouseX, mouseY);
         offset += configComponent.height + 5.0F;
      }

      float scrollMax = Math.max(offset1, offset2);
      if (scrollMax > 250.0F) {
         this.scrollY = MathHelper.clamp(this.scrollY, -scrollMax + 277.0F, 32.5F);
      } else {
         this.scrollY = 32.5F;
      }

   }

   private void drawComponents(int mouseX, int mouseY) {
      List<ModuleComponent> categoryModules1 = (List)modules.stream().filter((module) -> {
         return module.getModule().getCategory().equals(selected);
      }).collect(Collectors.toList());
      List<ModuleComponent> categoryModules2 = (List)modules2.stream().filter((module) -> {
         return module.getModule().getCategory().equals(selected);
      }).collect(Collectors.toList());
      this.scrollY += (float)Mouse.getDWheel() / 10.0F;
      float offset = this.scrollY;
      float offset1 = 32.5F;
      float offset2 = 32.5F;
      Iterator var8 = categoryModules1.iterator();

      while(true) {
         ModuleComponent moduleElement;
         Iterator var10;
         Component element;
         do {
            if (!var8.hasNext()) {
               offset = this.scrollY;
               var8 = categoryModules2.iterator();

               while(true) {
                  do {
                     if (!var8.hasNext()) {
                        float scrollMax = Math.max(offset1, offset2);
                        if (scrollMax > 250.0F) {
                           this.scrollY = MathHelper.clamp(this.scrollY, -scrollMax + 277.0F, 32.5F);
                        } else {
                           this.scrollY = 32.5F;
                        }

                        return;
                     }

                     moduleElement = (ModuleComponent)var8.next();
                     moduleElement.setX((float)(this.x + 224));
                     moduleElement.setY(AnimationMath.fast(moduleElement.y, (float)this.y + offset, 15.0F));
                  } while(!moduleElement.getModule().isSearched());

                  var10 = moduleElement.elements.iterator();

                  while(var10.hasNext()) {
                     element = (Component)var10.next();
                     if (element.isVisible()) {
                        offset += element.height;
                        offset2 += element.height;
                     }
                  }

                  moduleElement.render(mouseX, mouseY);
                  offset += moduleElement.height + 5.0F;
                  offset2 += moduleElement.height + 5.0F;
               }
            }

            moduleElement = (ModuleComponent)var8.next();
            moduleElement.setX((float)(this.x + 100));
            moduleElement.setY(AnimationMath.fast(moduleElement.y, (float)this.y + offset, 15.0F));
         } while(!moduleElement.getModule().isSearched());

         var10 = moduleElement.elements.iterator();

         while(var10.hasNext()) {
            element = (Component)var10.next();
            if (element.isVisible()) {
               offset += element.height;
               offset1 += element.height;
            }
         }

         moduleElement.render(mouseX, mouseY);
         offset += moduleElement.height + 5.0F;
         offset1 += moduleElement.height + 5.0F;
      }
   }

   public static void updateConfigComponents() {
      configs.clear();
      configs2.clear();
      List<Config> allConfigs = ConfigManager.getLoadedConfigs();
      Iterator var1 = allConfigs.iterator();

      while(var1.hasNext()) {
         Config config = (Config)var1.next();
         if (allConfigs.indexOf(config) % 2 == 0) {
            configs.add(new ConfigComponent(config.getName(), 120.0F, 30.0F));
         }

         if (allConfigs.indexOf(config) % 2 != 0) {
            configs2.add(new ConfigComponent(config.getName(), 120.0F, 30.0F));
         }
      }

   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      if (mouseButton == 0) {
         Category[] var4 = Category.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Category category = var4[var6];
            boolean hovered = RenderUtility.isHovered((float)mouseX, (float)mouseY, this.x + 4, (float)(this.y + 20 + 20 * category.ordinal() + 1 + (category.isBottom() ? 15 : 0)), 87.0F, 17.5F);
            if (hovered && !selected.equals(category)) {
               selected = category;
               this.scrollY = 32.5F;
               moduleAnimation.reset();
            }
         }
      }

      search.mouseClicked(mouseX, mouseY, mouseButton);
      if (colorPicker != null) {
         colorPicker.mouseClicked((double)mouseX, (double)mouseY, mouseButton);
      } else {
         Iterator var9 = modules.iterator();

         ModuleComponent moduleComponent;
         while(var9.hasNext()) {
            moduleComponent = (ModuleComponent)var9.next();
            if (moduleComponent.getModule().getCategory().equals(selected) && moduleComponent.getModule().isSearched()) {
               moduleComponent.mouseClicked((double)mouseX, (double)mouseY, mouseButton);
            }
         }

         var9 = modules2.iterator();

         while(var9.hasNext()) {
            moduleComponent = (ModuleComponent)var9.next();
            if (moduleComponent.getModule().getCategory().equals(selected) && moduleComponent.getModule().isSearched()) {
               moduleComponent.mouseClicked((double)mouseX, (double)mouseY, mouseButton);
            }
         }

         if (selected.equals(Category.THEMES)) {
            var9 = guiThemes.iterator();

            ThemeComponent themeComponent;
            while(var9.hasNext()) {
               themeComponent = (ThemeComponent)var9.next();
               themeComponent.mouseClicked((double)mouseX, (double)mouseY, mouseButton);
            }

            var9 = themes.iterator();

            while(var9.hasNext()) {
               themeComponent = (ThemeComponent)var9.next();
               themeComponent.mouseClicked((double)mouseX, (double)mouseY, mouseButton);
            }
         }

         if (selected.equals(Category.CONFIGS)) {
            var9 = configs.iterator();

            ConfigComponent configComponent;
            while(var9.hasNext()) {
               configComponent = (ConfigComponent)var9.next();
               if (configComponent.mouseBoolClicked((double)mouseX, (double)mouseY, mouseButton)) {
                  updateConfigComponents();
                  break;
               }
            }

            var9 = configs2.iterator();

            while(var9.hasNext()) {
               configComponent = (ConfigComponent)var9.next();
               if (configComponent.mouseBoolClicked((double)mouseX, (double)mouseY, mouseButton)) {
                  updateConfigComponents();
                  break;
               }
            }
         }

      }
   }

   protected void mouseReleased(int mouseX, int mouseY, int state) {
      super.mouseReleased(mouseX, mouseY, state);
      if (colorPicker != null) {
         colorPicker.mouseReleased((double)mouseX, (double)mouseY, state);
      }

   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      Iterator var3 = modules.iterator();

      ModuleComponent moduleComponent;
      while(var3.hasNext()) {
         moduleComponent = (ModuleComponent)var3.next();
         if (moduleComponent.getModule().getCategory().equals(selected) && moduleComponent.getModule().isSearched()) {
            moduleComponent.keyTyped(keyCode);
         }
      }

      var3 = modules2.iterator();

      while(var3.hasNext()) {
         moduleComponent = (ModuleComponent)var3.next();
         if (moduleComponent.getModule().getCategory().equals(selected) && moduleComponent.getModule().isSearched()) {
            moduleComponent.keyTyped(keyCode);
         }
      }

      search.textboxKeyTyped(typedChar, keyCode);
      if (keyCode == 1 && !escapeInUse) {
         search.setFocused(false);
         this.openAnimation.setDirection(Direction.BACKWARDS);
         this.openAnimation.setDuration(300);
         this.isClosed = true;
         SoundUtility.playSound("guidisabledev2.wav", 1.0f);
      } else {
         escapeInUse = false;
      }
   }

   public void onGuiClosed()
   {

   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   public static float getAnimationAlpha() {
      return moduleAnimation.getOutput();
   }

   static {
      selected = Category.COMBAT;
      modules = new ArrayList();
      modules2 = new ArrayList();
      guiThemes = new ArrayList();
      themes = new ArrayList();
      configs = new ArrayList();
      configs2 = new ArrayList();
      moduleAnimation = new DecelerateAnimation(500, 1.0F, Direction.BACKWARDS);
   }
}
