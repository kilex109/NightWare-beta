package nightware.main.ui.menu.main.newgui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import nightware.main.NightWare;
import nightware.main.manager.theme.Themes;
import nightware.main.ui.menu.widgets.CustomButton;
import nightware.main.ui.menu.widgets.newbuttons.NewButtons;
import nightware.main.utility.misc.HostsUtils;
import nightware.main.utility.misc.KillUtil;
import nightware.main.utility.misc.ScriptManager;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.font.Fonts;

import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;

public class NewRMLGui extends GuiScreen {
   boolean HaveSub;
   public void initGui() {
      this.width = (new ScaledResolution(mc)).getScaledWidth();
      this.height = (new ScaledResolution(mc)).getScaledHeight();
      if (NightWare.getInstance().getUserInfo().getRole().endsWith("+")) {
         HaveSub = true;
         this.addButton(new NewButtons(0, this.width / 2 - 105, this.height / 2 - 35, 100, 25, "Добавить", "Добавляет исключения."));
         this.addButton(new NewButtons(1, this.width / 2 + 5, this.height / 2 - 35, 100, 25, "Удалить", "Удаляет исключения."));
         this.addButton(new NewButtons(2, this.width / 2 - 105, this.height / 2 - 1, 100, 25, "Старт", "Запускает обход."));
         this.addButton(new NewButtons(3, this.width / 2 + 5, this.height / 2 - 1, 100, 25, "Стоп", "Останавливает обход."));
         this.addButton(new NewButtons(4, this.width / 2 - 105, this.height / 2 + 33, 210, 25, "Зайти с обходом", "Подключается на сервер с обходом."));
      } else {
         HaveSub = false;
      }
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      ScaledResolution sr = new ScaledResolution(this.mc);
      boolean isDark = NightWare.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
      int shadawcolar = isDark ? new Color(33, 33, 33).getRGB() : new Color(255, 255, 255).getRGB();
      RenderUtility.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0x111111).getRGB());
      RenderUtility.drawRoundedRect(sr.getScaledWidth() / 2 - 114, sr.getScaledHeight() / 2 - 46, 228, 112, 5, new Color(0x222222).getRGB());
      RenderUtility.drawFixedGlow(sr.getScaledWidth() / 2 - 114, sr.getScaledHeight() / 2 - 46, 228, 112, 10, shadawcolar);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      switch (button.id) {
         case 0:
            HostsUtils.addHostsEntry(InetAddress.getLocalHost().getHostAddress() + " lplay.rustme.net");
            break;
         case 1:
             HostsUtils.removeLineFromFile("lplay.rustme.net");
            break;
         case 2:
            ScriptManager.onBypass(mc.session.getUsername());
            break;
         case 3:
            KillUtil.killProcess("node.exe");
            break;
         case 4:
            this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, new ServerData("RustMe", InetAddress.getLocalHost().getHostAddress(), false)));
            break;
      }

      super.actionPerformed(button);
   }
}
