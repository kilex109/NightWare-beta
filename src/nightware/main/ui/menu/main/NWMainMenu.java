package nightware.main.ui.menu.main;

import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import nightware.main.NightWare;
import nightware.main.ui.menu.altmanager.GuiAltManager;
import nightware.main.ui.menu.widgets.CustomButton;
import nightware.main.utility.math.Vec2i;
import nightware.main.utility.misc.HostsUtils;
import nightware.main.utility.misc.KillUtil;
import nightware.main.utility.misc.ScriptManager;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.font.Fonts;
import java.awt.Color;
import java.awt.desktop.OpenFilesEvent;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.gui.ScaledResolution;

public class NWMainMenu extends GuiScreen {
   public void initGui() {
      this.width = (new ScaledResolution(mc)).getScaledWidth();
      this.height = (new ScaledResolution(mc)).getScaledHeight();
      this.addButton(new CustomButton(0, this.width / 2 - 102, this.height / 2 - 35, 100, 22, "Одиночная игра"));
      this.addButton(new CustomButton(1, this.width / 2 + 2, this.height / 2 - 35, 100, 22, "Зайти на RustMe"));
      this.addButton(new CustomButton(2, this.width / 2 - 102, this.height / 2 - 9, 100, 22, "Аккаунты"));
      this.addButton(new CustomButton(3, this.width / 2 + 2, this.height / 2 - 9, 100, 22, "Настройки"));
      this.addButton(new CustomButton(4, this.width / 2 - 102, this.height / 2 + 17, 204, 22, "Выход"));

      if(NightWare.getInstance().getUserInfo().getRole().equals("BetaFull") || NightWare.getInstance().getUserInfo().getRole().equals("Dev")) {
         this.addButton(new CustomButton(5, this.width / 2 - 102, this.height - 46, 204, 22, "Зайти с обходом"));
         this.addButton(new CustomButton(6, this.width / 2 - 102, this.height - 71, 204, 22, "Добавить"));
         this.addButton(new CustomButton(7, this.width / 2 - 102, this.height - 96, 204, 22, "Удалить"));
         this.addButton(new CustomButton(8, this.width / 2 - 102, this.height - 121, 204, 22, "Старт"));
         this.addButton(new CustomButton(9, this.width / 2 - 102, this.height - 146, 204, 22, "Стоп"));
      }
      if(NightWare.getInstance().getUserInfo().getRole().equals("Dev")) {
         this.addButton(new CustomButton(10, this.width / 2 - 102, this.height - 171, 204, 22, "Open Hosts (Debug)"));
      }
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      int scaledWidth = (new ScaledResolution(mc)).getScaledWidth();
      int scaledHeight = (new ScaledResolution(mc)).getScaledHeight();
      Color color = NightWare.getInstance().getC(0);
      Color color2 = NightWare.getInstance().getC(500);
      GlStateManager.pushMatrix();
      RenderUtility.drawRect(0.0F, 0.0F, (float)scaledWidth, (float)scaledHeight, (new Color(20, 20, 20)).getRGB());

      RenderUtility.applyRound(0.0F, 0.0F, 17.0F, 1.0F, () -> {
         RenderUtility.drawProfile((float)(scaledWidth), 10.0F, 35.0F, 35.0F);
      });

      Fonts.mntsb17.drawGradientString("User: " + NightWare.getInstance().getUserInfo().getName(), (scaledWidth - 10 - Fonts.mntsb17.getStringWidth("User: " + NightWare.getInstance().getUserInfo().getName())), 10.0F, NightWare.getInstance().getC(0), NightWare.getInstance().getC(250));
      Fonts.mntsb17.drawGradientString("UID: " + NightWare.getInstance().getUserInfo().getUid(), (scaledWidth - 10 - Fonts.mntsb17.getStringWidth("UID: " + NightWare.getInstance().getUserInfo().getUid())), 20.0F, NightWare.getInstance().getC(0), NightWare.getInstance().getC(250));
      Fonts.mntsb17.drawGradientString("Role: " + NightWare.getInstance().getUserInfo().getRole(), (scaledWidth - 10 - Fonts.mntsb17.getStringWidth("Role: " + NightWare.getInstance().getUserInfo().getRole())), 30.0F, NightWare.getInstance().getC(0), NightWare.getInstance().getC(250));
      Fonts.mntsb17.drawGradientString("Till: " + NightWare.getInstance().getUserInfo().getTill(), (scaledWidth - 10 - Fonts.mntsb17.getStringWidth("Till: " + NightWare.getInstance().getUserInfo().getTill())), 40.0F, NightWare.getInstance().getC(0), NightWare.getInstance().getC(250));
      Fonts.tenacityBold35.drawGradientCenteredString(NightWare.NAME, ((float)scaledWidth / 2.0F), ((float)scaledHeight / 2.0F - 65.0F), color, color2);
      super.drawScreen(mouseX, mouseY, partialTicks);
      GlStateManager.popMatrix();
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      switch (button.id) {
         case 0:
            this.mc.displayGuiScreen(new GuiWorldSelection(this));
            break;
         case 1:
            /*this.mc.displayGuiScreen(new GuiMultiplayer(this));*/
            this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, new ServerData("RustMe", "play.rustme.net", false)));
            break;
         case 2:
            this.mc.displayGuiScreen(new GuiAltManager());
            break;
         case 3:
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
            break;
         case 4:
            this.mc.shutdown();
            break;
         case 5:
            this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, new ServerData("RustMe", InetAddress.getLocalHost().getHostAddress(), false)));
            break;
         case 6:
            HostsUtils.addHostsEntry(InetAddress.getLocalHost().getHostAddress() + " lplay.rustme.net");
            break;
         case 7:
             HostsUtils.removeLineFromFile(InetAddress.getLocalHost().getHostAddress() + " lplay.rustme.net");
            break;
         case 8:
            ScriptManager.onBypass(mc.session.getUsername());
            break;
         case 9:
            KillUtil.killProcess("node.exe");
            break;
         case 10:

      }

      super.actionPerformed(button);
   }
}
