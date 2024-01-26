package nightware.main.ui.menu.altmanager;

import nightware.main.NightWare;
import nightware.main.ui.menu.altmanager.alt.Alt;
import nightware.main.ui.menu.altmanager.alt.AltLoginThread;
import nightware.main.ui.menu.altmanager.alt.AltManager;
import nightware.main.ui.menu.main.NWMainMenu;
import nightware.main.ui.menu.main.newgui.NewMainMenu;
import nightware.main.ui.menu.widgets.CustomButton;
import nightware.main.utility.math.Vec2i;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.StencilUtility;
import nightware.main.utility.render.font.Fonts;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.RandomStringUtils;
import org.lwjgl.input.Mouse;

public class GuiAltManager extends GuiScreen {
   public Alt selectedAlt = null;
   private AltLoginThread loginThread;
   public int scrollY = 0;
   private ResourceLocation resourceLocation;

   public void initGui() {
      ScaledResolution scaledResolution = new ScaledResolution(this.mc);
      int scaledWidth = scaledResolution.getScaledWidth();
      int scaledHeight = scaledResolution.getScaledHeight();
      this.addButton(new CustomButton(0, scaledWidth / 2 - 121, scaledHeight - 48, 57, 20, "Добавить"));
      this.addButton(new CustomButton(1, scaledWidth / 2 - 59, scaledHeight - 48, 57, 20, "Случайный"));
      this.addButton(new CustomButton(2, scaledWidth / 2 + 3, scaledHeight - 48, 57, 20, "Удалить"));
      this.addButton(new CustomButton(3, scaledWidth / 2 + 65, scaledHeight - 48, 57, 20, "Очистить"));
      this.addButton(new CustomButton(4, scaledWidth / 2 - 121, scaledHeight - 24, 243, 20, "Вернуться"));
      super.initGui();
   }

   private void getDownloadImageSkin(Alt alt, String username) {
      TextureManager textureManager = this.mc.getTextureManager();
      textureManager.getTexture(alt.getSkin());
      ThreadDownloadImageData textureObject = new ThreadDownloadImageData((File)null, String.format("https://minotar.net/skin/%s", StringUtils.stripControlCodes(username)), DefaultPlayerSkin.getDefaultSkin(AbstractClientPlayer.getOfflineUUID(username)), new ImageBufferDownload());
      textureManager.loadTexture(alt.getSkin(), textureObject);
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      switch(button.id) {
      case 0:
         this.mc.displayGuiScreen(new GuiAddAlt());
         break;
      case 1:
         String randomName = RandomStringUtils.randomNumeric(6) + "_NWB";
         AltManager.addAccount(new Alt(randomName, "", Alt.getCurrentDate()));
         this.mc.session = new Session(randomName, "", "", "mojang");
         break;
      case 2:
         if (this.loginThread != null) {
            this.loginThread = null;
         }

         AltManager.removeAccount(this.selectedAlt);
         this.selectedAlt = null;
         break;
      case 3:
         AltManager.clearAccounts();
         break;
      case 4:
         this.mc.displayGuiScreen(new NewMainMenu());
         break;
      }

      super.actionPerformed(button);
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      ScaledResolution scaledResolution = new ScaledResolution(this.mc);
      int scaledWidth = scaledResolution.getScaledWidth();
      int scaledHeight = scaledResolution.getScaledHeight();
      int color = NightWare.getInstance().getC(0).getRGB();
      int color2 = NightWare.getInstance().getC(500).getRGB();
      GlStateManager.pushMatrix();
      RenderUtility.drawRect(0.0F, 0.0F, (float)scaledWidth, (float)scaledHeight, (new Color(20, 20, 20)).getRGB());

      this.scrollY = (int)((float)this.scrollY + (float)Mouse.getDWheel() / 10.0F);
      int offset = this.scrollY;
      StencilUtility.initStencilToWrite();
      RenderUtility.drawRect((float)scaledWidth / 2.0F - 100.5F, 0.0F, 201.0F, (float)(scaledHeight - 50), 3);
      StencilUtility.readStencilBuffer(1);

      for(Iterator var19 = AltManager.registry.iterator(); var19.hasNext(); offset += 40) {
         Alt alt = (Alt)var19.next();
         if (alt.equals(this.selectedAlt)) {
            RenderUtility.drawRoundedGradientRect((float)scaledWidth / 2.0F - 100.5F, (float)(20 + offset) - 0.5F, 201.0F, 36.0F, 7.0F, 1.0F, color, color, color2, color2);
         }

         RenderUtility.drawRoundedRect((float)scaledWidth / 2.0F - 100.0F, (float)(20 + offset), 200.0F, 35.0F, 6.0F, (new Color(30, 30, 30)).getRGB());
         GlStateManager.pushMatrix();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         if (alt.getSkin() == null) {
            alt.setSkin(AbstractClientPlayer.getLocationSkin(alt.getUsername()));
            this.getDownloadImageSkin(alt, alt.getUsername());
         } else {
            this.mc.getTextureManager().bindTexture(alt.getSkin());
            GlStateManager.enableTexture2D();
            Gui.drawScaledCustomSizeModalRect(scaledWidth / 2 - 95, 25 + offset, 8.0F, 8.0F, 8, 8, 25, 25, 64.0F, 64.0F);
         }

         GlStateManager.popMatrix();
         String name = alt.getUsername();
         if (name.equalsIgnoreCase(this.mc.session.getUsername())) {
            name = TextFormatting.GREEN + name;
         }

         Fonts.mntsb16.drawString(name, (float)scaledWidth / 2.0F - 65.0F, (float)(30 + offset), -1);
         Fonts.mntsb13.drawString(alt.getDate(), (float)scaledWidth / 2.0F - 65.0F, 42.5F + (float)offset, -1);
      }

      StencilUtility.uninitStencilBuffer();
      super.drawScreen(mouseX, mouseY, partialTicks);
      GlStateManager.popMatrix();
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      ScaledResolution scaledResolution = new ScaledResolution(this.mc);
      int scaledHeight = scaledResolution.getScaledHeight();
      int scaledWidth = scaledResolution.getScaledWidth();

      this.scrollY = (int)((float)this.scrollY + (float)Mouse.getDWheel() / 10.0F);
      int offset = this.scrollY;

      for(Iterator var16 = AltManager.registry.iterator(); var16.hasNext(); offset += 40) {
         Alt alt = (Alt)var16.next();
         if (RenderUtility.isHovered(mouseX, mouseY, (float)scaledWidth / 2.0F - 100.0F, (float)(20 + offset), 200.0F, 35.0F)) {
            if (this.selectedAlt == alt) {
               (this.loginThread = new AltLoginThread(this.selectedAlt)).start();
               return;
            }

            this.selectedAlt = alt;
         }
      }

      super.mouseClicked(mouseX, mouseY, mouseButton);
   }
}
