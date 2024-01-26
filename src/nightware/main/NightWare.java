package nightware.main;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import lombok.Getter;
import nightware.main.command.CommandManager;
import nightware.main.event.input.EventInputKey;
import nightware.main.event.input.EventMouse;
import nightware.main.manager.config.ConfigManager;
import nightware.main.manager.dragging.DragManager;
import nightware.main.manager.friend.FriendManager;
import nightware.main.manager.lastAccount.LastAccountManager;
import nightware.main.manager.macro.MacroManager;
import nightware.main.manager.theme.ThemeManager;
import nightware.main.module.Module;
import nightware.main.module.ModuleManager;
import nightware.main.module.impl.render.Arraylist;
import nightware.main.ui.csgui.CsGui;
import nightware.main.ui.menu.altmanager.alt.AltFileManager;
import nightware.main.utility.misc.BullingUtility;
import nightware.main.utility.misc.Discord;
import nightware.main.utility.render.ColorUtility;
import nightware.main.utility.render.model.WingsLayerRender;
import nightware.main.utility.render.model.WingsModel;
import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import ru.crashdami.internalprotection.nativeobfuscator.Native;
import ru.crashdami.internalprotection.nativeobfuscator.NotNative;

@Native
public class NightWare {
   public static String NAME = "NightWare";
   public static String BUILD_TYPE = "Beta";
   private static final NightWare instance = new NightWare();
   @Getter
   private ModuleManager moduleManager;
   @Getter
   private final ConfigManager configManager = new ConfigManager();
   @Getter
   private ThemeManager themeManager;
   @Getter
   private CommandManager commandManager;
   @Getter
   private AltFileManager altFileManager;
   @Getter
   private LastAccountManager lastAccountManager;
   @Getter
   private FriendManager friendManager;
   @Getter
   private BullingUtility bullingUtility;
   @Getter
   private MacroManager macroManager;
   @Getter
   private DragManager dragManager;
   @Getter
   private CsGui csGui;
   private UserInfo userInfo;
   public static String username;
   public static int uid;
   public static String role;
   public static String till;

   public void start() throws IOException {
      System.out.println("Инициализация пользователя.. | 1/15");
      AvtorizaciyaHAHA.Server();
      this.userInfo = new UserInfo(username, uid, role, till);
      System.out.println("Инициализация тем клиента.. | 2/15");
      this.themeManager = new ThemeManager();
      System.out.println("Инициализация модулей.. | 3/15");
      this.moduleManager = new ModuleManager();
      System.out.println("Инициализация комманд.. | 4/15");
      this.commandManager = new CommandManager();

      try {
         System.out.println("Инициализация конфигов.. | 5/15");
         this.configManager.load();
         this.configManager.loadConfig("autocfg");
         System.out.println("Инициализация аккаунтов.. | 6/15");
         this.altFileManager = new AltFileManager();
         this.altFileManager.init();
         System.out.println("Инициализация последнего аккаунта.. | 7/15");
         this.lastAccountManager = new LastAccountManager();
         this.lastAccountManager.init();
         System.out.println("Инициализация передвижения худа.. | 8/15");
         this.dragManager = new DragManager();
         this.dragManager.init();
         System.out.println("Инициализация списка друзей.. | 9/15");
         this.friendManager = new FriendManager();
         this.friendManager.init();
         System.out.println("Инициализация макросов.. | 10/15");
         this.macroManager = new MacroManager();
         this.macroManager.init();
      } catch (Exception ignored) {
      }
      System.out.println("Инициализация активности в дискорде.. | 11/15");
      Discord.startRPC();
      System.out.println("Инициализация кликгуи.. | 12/15");
      this.csGui = new CsGui();
      System.out.println("Инициализация крыльев.. | 13/15");
      new WingsModel();

       for (RenderPlayer render : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) {
           render.addLayer(new WingsLayerRender());
       }

      System.out.println("Инициализация эвентов.. | 14/15");
      EventManager.register(this);
      System.out.println("Инициализация буллинга.. | 15/15");
      this.bullingUtility = new BullingUtility();
      this.bullingUtility.init();
      if (username.equals("null") || till.equals("null") || role.equals("null")) {
         System.exit((new Random()).nextInt(20));
      }
   }

   @NotNative
   public void reload() {
      try {
         AvtorizaciyaHAHA.Server();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
      this.userInfo = new UserInfo(username, uid, role, till);
   }

   @NotNative
   public void antiDebug() {
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
         killProcess("cheatengine-x86_64-SSE4-AVX2.exe");
         killProcess("cheatengine-i386.exe");
      } catch (IOException e) {
         for (; ; ) ;
      }
   }

   @NotNative
   public void killProcess(String name) throws IOException {
      Runtime.getRuntime().exec("taskkill /f /im " + name);
   }

   @EventTarget
   @NotNative
   public void onInputKey(EventInputKey eventInputKey) {
      this.dragManager.save();
      this.altFileManager.saveAll();
      this.lastAccountManager.save();

       for (Module module : this.moduleManager.getModules()) {

           if (module.getBind() == eventInputKey.getKey()) {
               module.toggle();
           }
       }

      this.macroManager.onKeyPressed(eventInputKey.getKey());
   }

   @EventTarget
   @NotNative
   public void onMouse(EventMouse eventMouse) {

       for (Module module : this.moduleManager.getModules()) {
           if (module.getMouseBind() == eventMouse.getButton() && eventMouse.getButton() > 2) {
               module.toggle();
           }
       }

      this.macroManager.onMousePressed(eventMouse.getButton());
   }

   @Native
   public UserInfo getUserInfo() {
      return this.userInfo;
   }

   @NotNative
   public static NightWare getInstance() {
      return instance;
   }

   @NotNative
   public Color getC(int index) {
      Color color = NightWare.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0];
      Color color2 = NightWare.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1];
      return ColorUtility.TwoColorEffect(color, color2, Arraylist.colorSpeed.get(), index);
   }
}
