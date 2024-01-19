package nightware.main.manager.config;

import com.darkmagician6.eventapi.EventManager;
import com.google.gson.JsonObject;
import nightware.main.NightWare;
import nightware.main.command.CommandManager;
import nightware.main.manager.theme.Themes;
import nightware.main.module.Module;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Config implements ConfigUpdater {
   private final String name;
   private final File file;

   public Config(String name) {
      this.name = name;
      this.file = new File(ConfigManager.configDirectory, name + ".nw");
      if (!this.file.exists()) {
         try {
            this.file.createNewFile();
         } catch (IOException var3) {
            var3.printStackTrace();
         }
      }

   }

   public JsonObject save() {
      JsonObject jsonObject = new JsonObject();
      JsonObject modulesObject = new JsonObject();
      Iterator var3 = NightWare.getInstance().getModuleManager().getModules().iterator();

      while(var3.hasNext()) {
         Module module = (Module)var3.next();
         modulesObject.add(module.name, module.save());
      }

      jsonObject.addProperty("Prefix", CommandManager.getPrefix());
      jsonObject.addProperty("GuiTheme", NightWare.getInstance().getThemeManager().getCurrentGuiTheme().getName());
      jsonObject.addProperty("StyleTheme", NightWare.getInstance().getThemeManager().getCurrentStyleTheme().getName());
      jsonObject.add("Modules", modulesObject);
      return jsonObject;
   }

   public void load(JsonObject object) {
      if (object.has("Prefix")) {
         CommandManager.setPrefix(object.get("Prefix").getAsString());
      }

      if (object.has("GuiTheme")) {
         NightWare.getInstance().getThemeManager().setCurrentGuiTheme(Themes.findByName(object.get("GuiTheme").getAsString()));
      }

      if (object.has("StyleTheme")) {
         NightWare.getInstance().getThemeManager().setCurrentStyleTheme(Themes.findByName(object.get("StyleTheme").getAsString()));
      }

      if (object.has("Prefix")) {
         CommandManager.setPrefix(object.get("Prefix").getAsString());
      }

      if (object.has("Modules")) {
         JsonObject modulesObject = object.getAsJsonObject("Modules");
         Iterator var3 = NightWare.getInstance().getModuleManager().getModules().iterator();

         while(var3.hasNext()) {
            Module module = (Module)var3.next();
            EventManager.unregister(module);
            module.load(modulesObject.getAsJsonObject(module.name));
         }
      }

   }

   public String getName() {
      return this.name;
   }

   public File getFile() {
      return this.file;
   }
}
