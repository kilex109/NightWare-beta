package nightware.main.ui.menu.altmanager;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.renderer.GlStateManager;
import nightware.main.NightWare;
import nightware.main.ui.menu.altmanager.alt.Alt;
import nightware.main.ui.menu.altmanager.alt.AltManager;
import nightware.main.ui.menu.widgets.CustomButton;
import nightware.main.ui.menu.widgets.PasswordField;
import nightware.main.ui.menu.widgets.TextField;
import nightware.main.utility.math.Vec2i;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.font.Fonts;
import nightware.main.utility.render.particle.Particle;
import java.awt.Color;
import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

public class GuiAddAlt extends GuiScreen {
   private TextField nameField;
   private PasswordField passwordField;
   private final ArrayList<Particle> particles = new ArrayList();
   private String status;
   private final GuiAltManager guiAltManager;

   protected GuiAddAlt() {
      this.status = TextFormatting.GRAY + "Add Alt";
      this.guiAltManager = new GuiAltManager();
   }

   public void initGui() {
      ScaledResolution scaledResolution = new ScaledResolution(this.mc);
      int scaledWidth = scaledResolution.getScaledWidth();
      int scaledHeight = scaledResolution.getScaledHeight();
      this.particles.clear();

      for(int i = 0; i < 100; ++i) {
         this.particles.add(new Particle());
      }

      Keyboard.enableRepeatEvents(true);
      this.nameField = new TextField(this.eventButton, Fonts.mntsb17, scaledWidth / 2 - 100, scaledHeight / 2 - 40, 200, 20);
      this.passwordField = new PasswordField(this.eventButton, Fonts.mntsb17, scaledWidth / 2 - 100, scaledHeight / 2 + 5, 200, 20);
      this.addButton(new CustomButton(0, scaledWidth / 2 - 100, scaledHeight / 2 + 40, 98, 20, "Добавить"));
      this.addButton(new CustomButton(1, scaledWidth / 2 + 2, scaledHeight / 2 + 40, 98, 20, "Вернуться"));
      super.initGui();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      ScaledResolution scaledResolution = new ScaledResolution(this.mc);
      int scaledWidth = scaledResolution.getScaledWidth();
      int scaledHeight = scaledResolution.getScaledHeight();
      GlStateManager.pushMatrix();
      RenderUtility.drawRect(0.0F, 0.0F, (float)scaledWidth, (float)scaledHeight, (new Color(20, 20, 20)).getRGB());
      this.particles.forEach((particle) -> {
         particle.update(mouseX, mouseY, scaledWidth, scaledHeight);
         particle.draw(this.particles, mouseX, mouseY);
      });
      Fonts.mntsb17.drawStringWithShadow("Ник / E-Mail", (float)scaledWidth / 2.0F - 100.0F, (float)scaledHeight / 2.0F - 52.0F, -7829368);
      this.nameField.drawTextBox();
      Fonts.mntsb17.drawStringWithShadow("Пароль", (float)scaledWidth / 2.0F - 100.0F, (float)scaledHeight / 2.0F - 7.0F, -7829368);
      this.passwordField.drawTextBox();
      Fonts.mntsb17.drawCenteredString(this.status, (float)scaledWidth / 2.0F, 10.0F, -1);
      super.drawScreen(mouseX, mouseY, partialTicks);
      GlStateManager.popMatrix();
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
      this.passwordField.mouseClicked(mouseX, mouseY, mouseButton);
      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      if (keyCode == 1) {
         this.mc.displayGuiScreen(new GuiAltManager());
      } else {
         this.nameField.textboxKeyTyped(typedChar, keyCode);
         this.passwordField.textboxKeyTyped(typedChar, keyCode);
         if (typedChar == '\t' && (this.nameField.isFocused() || this.passwordField.isFocused())) {
            this.nameField.setFocused(!this.nameField.isFocused());
            this.passwordField.setFocused(!this.passwordField.isFocused());
         }

         super.keyTyped(typedChar, keyCode);
      }
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      switch(button.id) {
      case 0:
         GuiAddAlt.AddAltThread login = new GuiAddAlt.AddAltThread(this.nameField.getText(), this.passwordField.getText());
         login.start();
         break;
      case 1:
         this.mc.displayGuiScreen(this.guiAltManager);
      }

      super.actionPerformed(button);
   }

   private static void setStatus(GuiAddAlt guiAddAlt, String status) {
      guiAddAlt.status = status;
   }

   private class AddAltThread extends Thread {
      private final String password;
      private final String username;

      AddAltThread(String username, String password) {
         this.username = username;
         this.password = password;
         GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.GRAY + "Add Alt");
      }

      private void checkAndAddAlt(String username, String password) {
         try {
            YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
            YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
            auth.setUsername(username);
            auth.setPassword(password);

            try {
               auth.logIn();
               AltManager.addAccount(new Alt(username, password, auth.getSelectedProfile().getName(), Alt.Status.Working, Alt.getCurrentDate()));
               GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.GREEN + "Добавлен аккаунт - " + TextFormatting.RED + this.username + TextFormatting.WHITE + " (Mojang)");
            } catch (AuthenticationException var6) {
               GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.RED + "Ошибка подключения!");
               var6.printStackTrace();
            }
         } catch (Throwable var7) {
            GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.RED + "Ошибка!");
            var7.printStackTrace();
         }

      }

      public void run() {
         if (this.password.equals("")) {
            AltManager.addAccount(new Alt(this.username, "", Alt.getCurrentDate()));
            GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.GREEN + "Добавлен аккаунт - " + TextFormatting.RED + this.username + TextFormatting.WHITE + " (без лицензии)");
         } else {
            GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.AQUA + "Подключение...");
            this.checkAndAddAlt(this.username, this.password);
         }

      }
   }
}
