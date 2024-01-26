package nightware.main.ui.menu.main.newgui;

import lombok.Getter;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import nightware.main.NightWare;
import nightware.main.manager.theme.Themes;
import nightware.main.ui.menu.altmanager.GuiAltManager;
import nightware.main.ui.menu.main.RMLBypassGui;
import nightware.main.ui.menu.widgets.CustomButton;
import nightware.main.ui.menu.widgets.newbuttons.NewButtons;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.blur.BlurUtility;
import nightware.main.utility.render.font.Fonts;

import java.awt.*;
import java.io.IOException;

public class NewMainMenu extends GuiScreen {

   public void initGui() {
      this.width = (new ScaledResolution(mc)).getScaledWidth();
      this.height = (new ScaledResolution(mc)).getScaledHeight();
      this.addButton(new NewButtons(0, this.width / 2 - 117, this.height / 2 - 54, 235, 25, "Одиночная игра", "Проверьте ваш конфиг на локальном сервере."));
      this.addButton(new NewButtons(1, this.width / 2 - 117, this.height / 2 - 20, 235, 25, "Зайти на RustMe", "Подключает вас на сервер."));
      this.addButton(new NewButtons(2, this.width / 2 - 117, this.height / 2 + 14, 235, 25, "Настройки", "Настройки майнкрафта."));
      this.addButton(new NewButtons(3, this.width / 2 - 117, this.height / 2 + 48, 235, 25, "Аккаунты", "Добавление и удаление аккаунтов."));

      if (NightWare.getInstance().getUserInfo().getRole().endsWith("+")) {
         this.addButton(new NewButtons(5, this.width / 2 - 117, this.height / 2 + 82, 115, 25, "Лаунчер", "Обход лаунчера."));
         this.addButton(new NewButtons(4, this.width / 2 + 2, this.height / 2 + 82, 115, 25, "Выход", "Закрывает игру."));
      } else {
         this.addButton(new NewButtons(4, this.width / 2 - 117, this.height / 2 + 82, 235, 25, "Выход", "Закрывает игру."));
      }
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      ScaledResolution sr = new ScaledResolution(this.mc);
      boolean isDark = NightWare.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
      int shadawcolar = isDark ? new Color(33, 33, 33).getRGB() : new Color(255, 255, 255).getRGB();
      int bakgraundcolar = isDark ? new Color(0x111111).getRGB() : new Color(0xE8E8E8).getRGB();
      RenderUtility.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0x111111).getRGB());
      RenderUtility.drawRoundedRect(sr.getScaledWidth() / 2 - 125, sr.getScaledHeight() / 2 - 100, 250, 215, 5, new Color(0x222222).getRGB());
      RenderUtility.drawFixedGlow(sr.getScaledWidth() / 2 - 125, sr.getScaledHeight() / 2 - 100, 250, 215, 10, shadawcolar);

      Fonts.mntsb32.drawCenteredString(NightWare.NAME, (sr.getScaledWidth() / 2.0F), (sr.getScaledHeight() / 2.0F - 85.0F), -1);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      switch (button.id) {
         case 0:
            this.mc.displayGuiScreen(new GuiWorldSelection(this));
            break;
         case 1:
//            this.mc.displayGuiScreen(new GuiMultiplayer(this));
            this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, new ServerData("RustMe", "play.rustme.net", false)));
            break;
         case 2:
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
            break;
         case 3:
            this.mc.displayGuiScreen(new GuiAltManager());
            break;
         case 4:
            this.mc.shutdown();
            break;
         case 5:
            this.mc.displayGuiScreen(new NewRMLGui());
            break;
      }

      super.actionPerformed(button);
   }
}
