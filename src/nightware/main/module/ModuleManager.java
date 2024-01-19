package nightware.main.module;

import nightware.main.module.impl.exploit.*;
import nightware.main.module.impl.movement.*;
import nightware.main.module.impl.player.AntiAFK;
import nightware.main.module.impl.player.AutoRespawn;
import nightware.main.module.impl.player.AutoTPAccept;
import nightware.main.module.impl.player.FreeCam;
import nightware.main.module.impl.player.InventoryMove;
import nightware.main.module.impl.player.MiddleClick;
import nightware.main.module.impl.player.NoPush;
import nightware.main.module.impl.player.NoServerRotations;
import nightware.main.module.impl.combat.HitEffect;
import nightware.main.module.impl.util.*;
import nightware.main.module.impl.combat.HitSound;
import nightware.main.module.impl.combat.*;
import nightware.main.module.impl.render.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ModuleManager {
   private final List<Module> modules = new ArrayList();
   public Timer timer;
   public Arraylist arraylist;

   public ModuleManager() {
      this.registerModule(new AutoSprint());
      this.registerModule(new AntiAim());
      this.registerModule(new AntiBot());
      this.registerModule(new BullingBot());
      this.registerModule(new WaterSpeed());
      this.registerModule(new Optimizer());
      this.registerModule(new Arrows());
      this.registerModule(new BackPeek());
      this.registerModule(new SuspendPackets());
      this.registerModule(new SlowPackets());
      this.registerModule(new NoSlow());
      this.registerModule(new Strafe());
      this.registerModule(this.timer = new Timer());
      this.registerModule(new SoundInfo());
      this.registerModule(this.arraylist = new Arraylist());
      this.registerModule(new TargetESP());
      this.registerModule(new Hud());
      this.registerModule(new ClickGuiModule());
      this.registerModule(new ChunkAnimator());
      this.registerModule(new Trails());
      this.registerModule(new ChinaHat());
      this.registerModule(new FullBright());
      this.registerModule(new JumpCircle());
      this.registerModule(new WallHack());
      this.registerModule(new HitEffect());
      this.registerModule(new WorldTime());
      this.registerModule(new Keybinds());
      this.registerModule(new Wings());
      this.registerModule(new NameTags());
      this.registerModule(new EntityESP());
      this.registerModule(new ItemESP());
      this.registerModule(new CameraClip());
      this.registerModule(new StaffList());
      this.registerModule(new Tracers());
      this.registerModule(new Crosshair());
      this.registerModule(new Notifications());
      this.registerModule(new NoHurtCam());
      this.registerModule(new AntiAFK());
      this.registerModule(new AutoRespawn());
      this.registerModule(new AutoTPAccept());
      this.registerModule(new NoPush());
      this.registerModule(new NoServerRotations());
      this.registerModule(new InventoryMove());
      this.registerModule(new MiddleClick());
      this.registerModule(new NoClip());
      this.registerModule(new FreeCam());
      this.registerModule(new NameProtect());
      this.registerModule(new BetterChat());
      this.registerModule(new BetterTab());
      this.registerModule(new HitSound());
      this.registerModule(new DeathCoordinates());
      this.registerModule(new PasswordHider());
      this.registerModule(new AimBot());
   }

   public void registerModule(Module module) {
      this.modules.add(module);
   }

   public List<Module> getModules() {
      return this.modules;
   }

   public Module[] getModulesFromCategory(Category category) {
      return (Module[])this.modules.stream().filter((module) -> {
         return module.category == category;
      }).toArray((x$0) -> {
         return new Module[x$0];
      });
   }

   public Module getModule(Class<? extends Module> classModule) {
      Iterator var2 = this.modules.iterator();

      Module module;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         module = (Module)var2.next();
      } while(module == null || module.getClass() != classModule);

      return module;
   }

   public Module getModule(String name) {
      Iterator var2 = this.modules.iterator();

      Module module;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         module = (Module)var2.next();
      } while(module == null || !module.getName().equalsIgnoreCase(name));

      return module;
   }
}
