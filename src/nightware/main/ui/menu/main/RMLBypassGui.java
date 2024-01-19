package nightware.main.ui.menu.main;

import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import nightware.main.NightWare;
import nightware.main.ui.menu.altmanager.GuiAltManager;
import nightware.main.ui.menu.widgets.CustomButton;
import nightware.main.utility.misc.HostsUtils;
import nightware.main.utility.misc.KillUtil;
import nightware.main.utility.misc.ScriptManager;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.font.Fonts;

import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;

public class RMLBypassGui extends GuiScreen {
   boolean HaveSub;
   public void initGui() {
      this.width = (new ScaledResolution(mc)).getScaledWidth();
      this.height = (new ScaledResolution(mc)).getScaledHeight();
      if (NightWare.getInstance().getUserInfo().getRole().equalsIgnoreCase("MEDIA+") || NightWare.getInstance().getUserInfo().getRole().equalsIgnoreCase("BETA+") || NightWare.getInstance().getUserInfo().getRole().equalsIgnoreCase("Dev")) {
         HaveSub = true;
         this.addButton(new CustomButton(0, this.width / 2 - 102, this.height / 2 - 35, 100, 22, "Добавить"));
         this.addButton(new CustomButton(1, this.width / 2 + 2, this.height / 2 - 35, 100, 22, "Удалить"));
         this.addButton(new CustomButton(2, this.width / 2 - 102, this.height / 2 - 9, 100, 22, "Старт"));
         this.addButton(new CustomButton(3, this.width / 2 + 2, this.height / 2 - 9, 100, 22, "Стоп"));
         this.addButton(new CustomButton(4, this.width / 2 - 102, this.height / 2 + 17, 204, 22, "Зайти с обходом"));
      } else {
         HaveSub = false;
      }
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      int scaledWidth = (new ScaledResolution(mc)).getScaledWidth();
      int scaledHeight = (new ScaledResolution(mc)).getScaledHeight();
      GlStateManager.pushMatrix();
      RenderUtility.drawRect(0.0F, 0.0F, (float)scaledWidth, (float)scaledHeight, (new Color(20, 20, 20)).getRGB());
      if (HaveSub) {
         Fonts.mntsb24.drawGradientCenteredString("Меню обхода лаунчера", scaledWidth / 2, 10, NightWare.getInstance().getC(0), NightWare.getInstance().getC(250));
      } else if (HaveSub) {
         Fonts.mntsb32.drawGradientCenteredString("У вас нету доступа к этому меню!", scaledHeight / 2, scaledHeight / 2, NightWare.getInstance().getC(0), NightWare.getInstance().getC(250));
      }
      super.drawScreen(mouseX, mouseY, partialTicks);
      GlStateManager.popMatrix();
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      switch (button.id) {
         case 0:
            HostsUtils.addHostsEntry(InetAddress.getLocalHost().getHostAddress() + " lplay.rustme.net");
            break;
         case 1:
             HostsUtils.removeLineFromFile(InetAddress.getLocalHost().getHostAddress() + " lplay.rustme.net");
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
