package nightware.main.ui.menu.proxy;

import com.mojang.realmsclient.gui.ChatFormatting;
import nightware.main.manager.proxy.Proxy;
import nightware.main.manager.proxy.ProxyManager;
import nightware.main.manager.proxy.TestPing;
import nightware.main.ui.menu.main.NWMainMenu;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;

public class GuiProxy extends GuiScreen {
   private boolean isSocks4 = false;
   private boolean isEnabled = false;
   private GuiTextField ipPort;
   private GuiTextField username;
   private GuiTextField password;
   private final GuiScreen parentScreen;
   private String msg = "";
   private int[] positionY;
   private int positionX;
   private TestPing testPing = new TestPing();

   public GuiProxy(GuiScreen parentScreen) {
      this.parentScreen = parentScreen;
   }

   private static boolean isValidIpPort(String ipP) {
      String[] split = ipP.split(":");
      if (split.length > 1) {
         if (!StringUtils.isNumeric(split[1])) {
            return false;
         } else {
            int port = Integer.parseInt(split[1]);
            return port >= 0 && port <= 65535;
         }
      } else {
         return false;
      }
   }

   private boolean checkProxy() {
      if (!isValidIpPort(this.ipPort.getText())) {
         this.msg = ChatFormatting.RED + "Неправильный IP:PORT";
         this.ipPort.setFocused(true);
         return false;
      } else {
         return true;
      }
   }

   private void centerButtons(int amount, int buttonLength, int gap) {
      this.positionX = this.width / 2 - buttonLength / 2;
      this.positionY = new int[amount];
      int center = (this.height + amount * gap) / 2;
      int buttonStarts = center - amount * gap;

      for(int i = 0; i != amount; ++i) {
         this.positionY[i] = buttonStarts + gap * i;
      }

   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      this.ipPort.textboxKeyTyped(typedChar, keyCode);
      this.username.textboxKeyTyped(typedChar, keyCode);
      this.password.textboxKeyTyped(typedChar, keyCode);
      this.msg = "";
      this.testPing.state = "";
      super.keyTyped(typedChar, keyCode);
   }

   public void updateScreen() {
      this.testPing.pingPendingNetworks();
      this.ipPort.updateCursorCounter();
      this.username.updateCursorCounter();
      this.password.updateCursorCounter();
      super.updateScreen();
   }

   public void initGui() {
      int buttonLength = 160;
      this.centerButtons(10, buttonLength, 26);
      this.isSocks4 = ProxyManager.proxy.type == Proxy.ProxyType.SOCKS4;
      GuiButton proxyType = new GuiButton(0, this.positionX, this.positionY[1], buttonLength, 20, this.isSocks4 ? "Socks 4" : "Socks 5");
      this.addButton(proxyType);
      this.ipPort = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.positionX, this.positionY[2], buttonLength, 20);
      this.ipPort.setText(ProxyManager.proxy.ipPort);
      this.ipPort.setMaxStringLength(1024);
      this.ipPort.setFocused(true);
      this.username = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.positionX, this.positionY[4], buttonLength, 20);
      this.username.setMaxStringLength(255);
      this.username.setText(ProxyManager.proxy.username);
      this.password = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.positionX, this.positionY[5], buttonLength, 20);
      this.password.setMaxStringLength(255);
      this.password.setText(ProxyManager.proxy.password);
      int posXButtons = this.width / 2 - buttonLength / 2 * 3 / 2;
      this.addButton(new GuiButton(1, posXButtons, this.positionY[8], buttonLength / 2 - 3, 20, "Применить"));
      this.addButton(new GuiButton(2, posXButtons + buttonLength / 2 + 3, this.positionY[8], buttonLength / 2 - 3, 20, "Проверить"));
      this.addButton(new GuiButton(3, this.positionX, this.positionY[7], buttonLength, 20, this.isEnabled ? "Включено" : "Выключено"));
      this.addButton(new GuiButton(4, posXButtons + (buttonLength / 2 + 3) * 2, this.positionY[8], buttonLength / 2 - 3, 20, "Вернуться"));
      super.initGui();
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      switch(button.id) {
      case 0:
         this.isSocks4 = !this.isSocks4;
         button.displayString = this.isSocks4 ? "Socks 4" : "Socks 5";
         break;
      case 1:
         if (this.checkProxy()) {
            ProxyManager.proxy = new Proxy(this.isSocks4, this.ipPort.getText(), this.username.getText(), this.password.getText());
            ProxyManager.proxyEnabled = this.isEnabled;
            this.mc.displayGuiScreen(new GuiMultiplayer(new NWMainMenu()));
         }
         break;
      case 2:
         if (!this.ipPort.getText().isEmpty() && !this.ipPort.getText().equalsIgnoreCase("none")) {
            if (this.checkProxy()) {
               this.testPing = new TestPing();
               this.testPing.run("play.rustme.net", 25565, new Proxy(this.isSocks4, this.ipPort.getText(), this.username.getText(), this.password.getText()));
            }
            break;
         }

         this.msg = TextFormatting.RED + "Укажите прокси для тестирования";
         return;
      case 3:
         if (isValidIpPort(this.ipPort.getText())) {
            this.isEnabled = !this.isEnabled;
            button.displayString = this.isEnabled ? "Включено" : "Выключено";
         }
         break;
      case 4:
         this.mc.displayGuiScreen(this.parentScreen);
      }

      super.actionPerformed(button);
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      this.drawString(this.mc.fontRendererObj, "Тип:", this.width / 2 - 150, this.positionY[1] + 5, 10526880);
      this.drawCenteredString(this.mc.fontRendererObj, "Аутентификация (необязательно)", this.width / 2, this.positionY[3] + 8, -1);
      this.drawString(this.mc.fontRendererObj, "IP:PORT: ", this.width / 2 - 150, this.positionY[2] + 5, 10526880);
      this.drawString(this.mc.fontRendererObj, "Статус: ", this.width / 2 - 150, this.positionY[7] + 5, 10526880);
      this.ipPort.drawTextBox();
      if (this.isSocks4) {
         this.drawString(this.mc.fontRendererObj, "User ID: ", this.width / 2 - 150, this.positionY[4] + 5, 10526880);
         this.username.drawTextBox();
      } else {
         this.drawString(this.mc.fontRendererObj, "Имя: ", this.width / 2 - 150, this.positionY[4] + 5, 10526880);
         this.drawString(this.mc.fontRendererObj, "Пароль: ", this.width / 2 - 150, this.positionY[5] + 5, 10526880);
         this.username.drawTextBox();
         this.password.drawTextBox();
      }

      this.drawCenteredString(this.mc.fontRendererObj, !this.msg.isEmpty() ? this.msg : this.testPing.state, this.width / 2, this.positionY[6] + 5, 10526880);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      this.ipPort.mouseClicked(mouseX, mouseY, mouseButton);
      this.username.mouseClicked(mouseX, mouseY, mouseButton);
      this.password.mouseClicked(mouseX, mouseY, mouseButton);
      super.mouseClicked(mouseX, mouseY, mouseButton);
   }
}
