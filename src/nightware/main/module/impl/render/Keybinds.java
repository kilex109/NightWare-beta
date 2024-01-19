package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.renderer.GlStateManager;
import nightware.main.NightWare;
import nightware.main.event.render.EventRender2D;
import nightware.main.manager.dragging.DragManager;
import nightware.main.manager.dragging.Draggable;
import nightware.main.manager.theme.Themes;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.utility.render.ColorUtility;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.SmartScissor;
import nightware.main.utility.render.animation.AnimationMath;
import nightware.main.utility.render.font.Fonts;
import java.awt.Color;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.input.Keyboard;

@ModuleAnnotation(
   name = "Keybinds",
   category = Category.RENDER
)
public class Keybinds extends Module {
   private final Draggable keybindsDraggable = DragManager.create(this, "Keybinds", 200, 200);
   public float realOffset = 0.0F;

   @EventTarget
   public void onRender2D(EventRender2D event) {
      if (!mc.gameSettings.showDebugInfo) {
         GlStateManager.pushMatrix();
         List<Module> sortedBinds = (List) NightWare.getInstance().getModuleManager().getModules().stream().filter((modulex) -> {
            return modulex.getBind() != 0 && modulex.isEnabled();
         }).sorted(Comparator.comparing((modulex) -> {
            return Fonts.nunitoBold15.getStringWidth(modulex.getName());
         }, Comparator.reverseOrder())).collect(Collectors.toList());
         int glowColor = NightWare.getInstance().getC(0).getRGB();
         int glowColor2 = NightWare.getInstance().getC(500).getRGB();
         boolean isDark = NightWare.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
         int offset = -1;
         int width = 105;
         Iterator var8;
         Module module;
         if (!sortedBinds.isEmpty()) {
            width = (Integer) sortedBinds.stream().max(Comparator.comparingInt((modulex) -> {
               return Fonts.nunitoBold15.getStringWidth(this.getText(modulex));
            })).map((modulex) -> {
               return Fonts.nunitoBold15.getStringWidth(this.getText(modulex));
            }).get() + 11;
            if (width < 105) {
               width = 105;
            }

            offset = 0;

            for (var8 = sortedBinds.iterator(); var8.hasNext(); offset += 11) {
               module = (Module) var8.next();
            }
         }

         this.realOffset = AnimationMath.fast(this.realOffset, (float) offset, 15.0F);
         this.keybindsDraggable.setWidth((float) width);
         this.keybindsDraggable.setHeight((float) (19 + offset));
         int bgColor = isDark ? new Color(30, 30, 30, 230).getRGB() : new Color(255, 255, 255, 220).getRGB();
         RenderUtility.drawGradientGlow((float) this.keybindsDraggable.getX(), (float) this.keybindsDraggable.getY(), (float) width, 19.0F + this.realOffset, 10, glowColor, glowColor2, glowColor, glowColor2);
         RenderUtility.Cornered((float) this.keybindsDraggable.getX(), (float) this.keybindsDraggable.getY(), (float) width, 19.0F + this.realOffset, 5, 5, 5, 5, bgColor);
         Fonts.nunitoBold18.drawGradientCenteredString("Клавиши", this.keybindsDraggable.getX() + (width / 2), (float) this.keybindsDraggable.getY() + 5.5F, NightWare.getInstance().getC(0), NightWare.getInstance().getC(500));
         SmartScissor.push();
         SmartScissor.setFromComponentCoordinates((double) this.keybindsDraggable.getX(), (double) this.keybindsDraggable.getY(), (double) width, (double) (19.0F + this.realOffset));
         offset = 0;

         for (var8 = sortedBinds.iterator(); var8.hasNext(); offset += 11) {
            module = (Module) var8.next();
            Fonts.nunitoBold15.drawString(module.getName(), (float) (this.keybindsDraggable.getX() + 5), (float) this.keybindsDraggable.getY() + 19.5F + (float) offset, isDark ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
            String bindText = "[" + (module.bind < 0 ? "MOUSE " + module.getMouseBind() : Keyboard.getKeyName(module.getBind())) + "]";
            Fonts.nunitoBold15.drawString(bindText, (float) (this.keybindsDraggable.getX() + width - Fonts.nunitoBold15.getStringWidth(bindText) - 5), (float) this.keybindsDraggable.getY() + 19.5F + (float) offset, isDark ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
         }

         SmartScissor.unset();
         SmartScissor.pop();
         GlStateManager.popMatrix();
      }
   }

   private String getText(Module module) {
      return module.getName() + " [" + (module.bind < 0 ? "MOUSE " + module.getMouseBind() : Keyboard.getKeyName(module.getBind())) + "]";
   }
}
