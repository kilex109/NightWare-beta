package nightware.main;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.util.text.TextFormatting;
import nightware.main.command.CommandManager;
import nightware.main.event.input.EventInputKey;
import nightware.main.event.input.EventMouse;
import nightware.main.manager.config.ConfigManager;
import nightware.main.manager.dragging.DragManager;
import nightware.main.manager.friend.FriendManager;
import nightware.main.manager.lastAccount.LastAccountManager;
import nightware.main.manager.macro.MacroManager;
import nightware.main.manager.proxy.ProxyManager;
import nightware.main.manager.staff.StaffManager;
import nightware.main.manager.theme.ThemeManager;
import nightware.main.manager.theme.Themes;
import nightware.main.module.Module;
import nightware.main.module.ModuleManager;
import nightware.main.ui.csgui.CsGui;
import nightware.main.ui.menu.altmanager.alt.AltFileManager;
import nightware.main.ui.menu.main.NWMainMenu;
import nightware.main.ui.menu.proxy.GuiProxy;
import nightware.main.utility.misc.Discord;
import nightware.main.utility.misc.ScriptManager;
import nightware.main.utility.render.ColorUtility;
import nightware.main.utility.render.model.WingsLayerRender;
import nightware.main.utility.render.model.WingsModel;
import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import ru.crashdami.internalprotection.nativeobfuscator.Native;
import ru.crashdami.internalprotection.nativeobfuscator.NotNative;

@Native
public class NightWare {
   public static String NAME = "NightWare";
   public static String VERSION = "1.0";
   public static String BUILD_TYPE = "Beta";
   public static String EDITION = "1.12.2 Edition";
   private static final NightWare instance = new NightWare();
   private ModuleManager moduleManager;
   private final ConfigManager configManager = new ConfigManager();
   private ThemeManager themeManager;
   private CommandManager commandManager;
   private AltFileManager altFileManager;
   private LastAccountManager lastAccountManager;
   private FriendManager friendManager;
   private StaffManager staffManager;
   private MacroManager macroManager;
   private ProxyManager proxyManager;
   private DragManager dragManager;
   private CsGui csGui;
   private UserInfo userInfo;
   private GuiProxy guiProxy;
   public static String username;
   public static int uid;
   public static String role;
   public static String till;

   public void start() throws IOException {
      System.out.println("Инициализация пользователя.. | 1/19");
      AvtorizaciyaHAHA.Server();
      this.userInfo = new UserInfo(username, uid, role, till);
      System.out.println("Инициализация тем клиента.. | 2/19");
      this.themeManager = new ThemeManager();
      System.out.println("Инициализация модулей.. | 3/19");
      this.moduleManager = new ModuleManager();
      System.out.println("Инициализация комманд.. | 4/19");
      this.commandManager = new CommandManager();

      try {
         killProcess("ollydbg.exe");
         killProcess("ProcessHacker.exe");
         killProcess("tcpview.exe");
         killProcess("autoruns.exe");
         killProcess("autorunsc.exe");
         killProcess("filemon.exe");
         killProcess("procmon.exe");
         killProcess("regmon.exe");
         killProcess("procexp.exe");
         killProcess("idaq.exe");
         killProcess("ida.exe");
         killProcess("idaq64.exe");
         killProcess("ImmunityDebugger.exe");
         killProcess("Wireshark.exe");
         killProcess("dumpcap.exe");
         killProcess("HookExplorer.exe");
         killProcess("ImportREC.exe");
         killProcess("PETools.exe");
         killProcess("LordPE.exe");
         killProcess("dumpcap.exe");
         killProcess("SysInspector.exe");
         killProcess("proc_analyzer.exe");
         killProcess("sysAnalyzer.exe");
         killProcess("sniff_hit.exe");
         killProcess("windbg.exe");
         killProcess("joeboxcontrol.exe");
         killProcess("joeboxserver.exe");
         killProcess("x32dbg.exe");
         killProcess("x64dbg.exe");
         killProcess("x96dbg.exe");
         killProcess("ida64.exe");
         killProcess("httpdebugger.exe");
         killProcess("cheatengine-x86_64.exe");

         System.out.println("Инициализация конфигов.. | 5/19");
         this.configManager.loadConfig("autocfg");
         System.out.println("Инициализация аккаунтов.. | 6/19");
         this.altFileManager = new AltFileManager();
         this.altFileManager.init();
         System.out.println("Инициализация последнего аккаунта.. | 7/19");
         this.lastAccountManager = new LastAccountManager();
         this.lastAccountManager.init();
         System.out.println("Инициализация передвижения худа.. | 8/19");
         this.dragManager = new DragManager();
         this.dragManager.init();
         System.out.println("Инициализация списка друзей.. | 9/19");
         this.friendManager = new FriendManager();
         this.friendManager.init();
         System.out.println("Инициализация списка админов.. | 10/19");
         this.staffManager = new StaffManager();
         this.staffManager.init();
         System.out.println("Инициализация макросов.. | 11/19");
         this.macroManager = new MacroManager();
         this.macroManager.init();
         System.out.println("Инициализация прокси.. | 12/19");
         this.proxyManager = new ProxyManager();
         this.proxyManager.init();
      } catch (Exception var3) {
         var3.printStackTrace();
      }
      System.out.println("Инициализация активности в дискорде.. | 13/19");
      Discord.startRPC();
      System.out.println("Инициализация прокси гуи.. | 14/19");
      this.guiProxy = new GuiProxy(new GuiMultiplayer(new NWMainMenu()));
      System.out.println("Инициализация кликгуи.. | 15/19");
      this.csGui = new CsGui();
      System.out.println("Инициализация крыльев.. | 16/19");
      new WingsModel();
      System.out.println("Инициализация рендера.. | 17/19");
      Iterator var1 = Minecraft.getMinecraft().getRenderManager().getSkinMap().values().iterator();

      while(var1.hasNext()) {
         RenderPlayer render = (RenderPlayer)var1.next();
         render.addLayer(new WingsLayerRender());
      }

      System.out.println("Инициализация эвентов.. | 18/19");
      EventManager.register(this);
      System.out.println("Инициализация потоков.. | 19/19");
      Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
     if (this.username.equals("null") || this.till.equals("null") || this.role.equals("null")) {
         System.exit((new Random()).nextInt(20));
      }
   }

   @NotNative
   public void reload() {
      this.themeManager = new ThemeManager();
      this.commandManager = new CommandManager();
   }

   @NotNative
   public void shutdown() {
      this.dragManager.save();
      this.proxyManager.save();
      this.altFileManager.saveAll();
      this.lastAccountManager.save();
      this.configManager.saveConfig("autocfg");
   }

   @NotNative
   public static TextFormatting clientPrefixC() {
      if (NightWare.getInstance().getThemeManager().getCurrentStyleTheme().equals(Themes.GOLD.getTheme())) {
         return TextFormatting.GOLD;
      } else if (NightWare.getInstance().getThemeManager().getCurrentStyleTheme().equals(Themes.MIDNIGHT.getTheme())) {
         return TextFormatting.DARK_GRAY;
      } else if (NightWare.getInstance().getThemeManager().getCurrentStyleTheme().equals(Themes.PURPLE.getTheme())) {
         return TextFormatting.LIGHT_PURPLE;
      } else if (NightWare.getInstance().getThemeManager().getCurrentStyleTheme().equals(Themes.NIGHT.getTheme())) {
         return TextFormatting.DARK_AQUA;
      } else if (NightWare.getInstance().getThemeManager().getCurrentStyleTheme().equals(Themes.DEEPBLUE.getTheme())) {
         return TextFormatting.BLUE;
      }
      return TextFormatting.BLACK;
   }

   @NotNative
   public void killProcess(String name) throws IOException {
      Runtime.getRuntime().exec("taskkill /f /im " + name);
   }

   @EventTarget
   @NotNative
   public void onInputKey(EventInputKey eventInputKey) {
      Iterator var2 = this.moduleManager.getModules().iterator();

      while(var2.hasNext()) {

         Module module = (Module)var2.next();
         if (module.getBind() == eventInputKey.getKey()) {
            module.toggle();
         }
      }

      this.macroManager.onKeyPressed(eventInputKey.getKey());
   }

   @EventTarget
   @NotNative
   public void onMouse(EventMouse eventMouse) {
      Iterator var2 = this.moduleManager.getModules().iterator();

      while(var2.hasNext()) {
         Module module = (Module)var2.next();
         if (module.getMouseBind() == eventMouse.getButton() && eventMouse.getButton() > 2) {
            module.toggle();
         }
      }

      this.macroManager.onMousePressed(eventMouse.getButton());
   }

   public ModuleManager getModuleManager() {
      return this.moduleManager;
   }

   public ConfigManager getConfigManager() {
      return this.configManager;
   }

   public ThemeManager getThemeManager() {
      return this.themeManager;
   }

   public CommandManager getCommandManager() {
      return this.commandManager;
   }

   public AltFileManager getAltFileManager() {
      return this.altFileManager;
   }

   public LastAccountManager getLastAccountManager() {
      return this.lastAccountManager;
   }

   public FriendManager getFriendManager() {
      return this.friendManager;
   }

   public StaffManager getStaffManager() {
      return this.staffManager;
   }

   public MacroManager getMacroManager() {
      return this.macroManager;
   }

   public ProxyManager getProxyManager() {
      return this.proxyManager;
   }

   public DragManager getDragManager() {
      return this.dragManager;
   }

   public CsGui getCsGui() {
      return this.csGui;
   }

   @Native
   public UserInfo getUserInfo() {
      return this.userInfo;
   }

   public GuiProxy getGuiProxy() {
      return this.guiProxy;
   }

   @NotNative
   public static NightWare getInstance() {
      return instance;
   }

   @NotNative
   public Color getC(int index) {
      Color color = NightWare.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0];
      Color color2 = NightWare.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1];
      return ColorUtility.TwoColorEffect(color, color2, getModuleManager().arraylist.colorSpeed.get(), index);
   }
}
