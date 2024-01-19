package nightware.main.module.impl.util;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.realmsclient.gui.ChatFormatting;
import nightware.main.event.packet.EventReceivePacket;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.BooleanSetting;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;

@ModuleAnnotation(
   name = "BetterChat",
   category = Category.UTIL
)
public class BetterChat extends Module {
   public static BooleanSetting chatHistory = new BooleanSetting("Сохранять историю", true);
   public static BooleanSetting changeChat = new BooleanSetting("Изменять чат", true);
}
