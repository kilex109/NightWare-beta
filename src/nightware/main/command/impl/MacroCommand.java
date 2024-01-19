package nightware.main.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextFormatting;
import nightware.main.NightWare;
import nightware.main.command.Command;
import nightware.main.command.CommandAbstract;
import nightware.main.manager.macro.Macro;
import nightware.main.utility.misc.StringUtility;
import java.util.Iterator;

import org.lwjgl.input.Keyboard;

@Command(
   name = "macro",
   description = "Позволяет отправить команду по нажатию кнопки"
)
public class MacroCommand extends CommandAbstract {
   public void error() {
      this.sendMessage(ChatFormatting.GRAY + "Ошибка в использовании" + ChatFormatting.WHITE + ":");
      this.sendMessage(ChatFormatting.WHITE + ".macro add " + ChatFormatting.GRAY + "<" + TextFormatting.GOLD + "key" + ChatFormatting.GRAY + "> <" + TextFormatting.GOLD + "message" + ChatFormatting.GRAY + ">");
      this.sendMessage(ChatFormatting.WHITE + ".macro remove " + ChatFormatting.GRAY + "<" + TextFormatting.GOLD + "key" + ChatFormatting.GRAY + ">");
      this.sendMessage(ChatFormatting.WHITE + ".macro list");
      this.sendMessage(ChatFormatting.WHITE + ".macro clear");
   }

   public void execute(String[] args) throws Exception {
      if (args.length > 1) {
         String var2 = args[1];
         byte var3 = -1;
         switch(var2.hashCode()) {
         case -934610812:
            if (var2.equals("remove")) {
               var3 = 2;
            }
            break;
         case 96417:
            if (var2.equals("add")) {
               var3 = 0;
            }
            break;
         case 3322014:
            if (var2.equals("list")) {
               var3 = 3;
            }
            break;
         case 94746189:
            if (var2.equals("clear")) {
               var3 = 1;
            }
         }

         int digit;
         switch(var3) {
         case 0:
            int keyIndex = Keyboard.getKeyIndex(args[2].toUpperCase());
            StringBuilder sb = new StringBuilder();

            for(int i = 3; i < args.length; ++i) {
               sb.append(args[i]).append(" ");
            }

            String message = sb.toString().trim();
            String redMessage = StringUtility.getStringRedColor(message);
            if (keyIndex == 0) {
               if (args[2].startsWith("mouse")) {
                  try {
                     digit = Integer.parseInt(args[2].replaceAll("mouse", ""));
                     if (digit < 100) {
                        NightWare.getInstance().getMacroManager().addMacros(new Macro(message, digit - 100));
                        this.sendMessage(ChatFormatting.GRAY + "Успешно добавлен макрос для кнопки мыши" + TextFormatting.GOLD + " \"" + digit + TextFormatting.GOLD + "\" " + ChatFormatting.GRAY + "с командой " + TextFormatting.GOLD + redMessage);
                     } else {
                        this.sendMessage(ChatFormatting.GRAY + "Кнопка мыши не может быть 100 и больше!");
                     }
                  } catch (NumberFormatException var12) {
                     this.error();
                  }
               } else {
                  this.sendMessage(ChatFormatting.GRAY + "Такой клавиши не существует!");
               }
            } else {
               NightWare.getInstance().getMacroManager().addMacros(new Macro(message, keyIndex));
               this.sendMessage(ChatFormatting.GRAY + "Успешно добавлен макрос для кнопки" + TextFormatting.GOLD + " \"" + args[2].toUpperCase() + TextFormatting.GOLD + "\" " + ChatFormatting.GRAY + "с командой " + TextFormatting.GOLD + redMessage);
            }
            break;
         case 1:
            if (NightWare.getInstance().getMacroManager().getMacros().isEmpty()) {
               this.sendMessage(ChatFormatting.GRAY + "Список макросов пуст.");
            } else {
               this.sendMessage(ChatFormatting.GRAY + "Список макросов успешно очищен!");
               NightWare.getInstance().getMacroManager().getMacros().clear();
               NightWare.getInstance().getMacroManager().updateFile();
            }
            break;
         case 2:
            digit = Keyboard.getKeyIndex(args[2].toUpperCase());
            if (digit == 0) {
               if (args[2].startsWith("mouse")) {
                  String digits = StringUtility.getDigits(args[2]);

                  try {
                     digit = Integer.parseInt(digits);
                     NightWare.getInstance().getMacroManager().deleteMacro(digit - 100);
                     this.sendMessage(ChatFormatting.GRAY + "Макрос был удален с кнопки " + TextFormatting.GOLD + "\"" + args[2] + "\"");
                  } catch (NumberFormatException var11) {
                     this.error();
                  }
               } else {
                  this.sendMessage(ChatFormatting.GRAY + "Такой клавиши не существует!");
               }
            } else {
               NightWare.getInstance().getMacroManager().deleteMacro(digit);
               this.sendMessage(ChatFormatting.GRAY + "Макрос был удален с кнопки " + TextFormatting.GOLD + "\"" + args[2].toUpperCase() + "\"");
            }
            break;
         case 3:
            if (NightWare.getInstance().getMacroManager().getMacros().isEmpty()) {
               this.sendMessage(ChatFormatting.GRAY + "Список макросов пуст.");
            } else {
               this.sendMessage(ChatFormatting.GREEN + "Список макросов: ");
               Iterator var9 = NightWare.getInstance().getMacroManager().getMacros().iterator();

               while(var9.hasNext()) {
                  Macro macro = (Macro)var9.next();
                  if (macro.getKey() < 0) {
                     this.sendMessage(ChatFormatting.WHITE + "Команда: " + TextFormatting.GOLD + macro.getMessage() + ChatFormatting.WHITE + ", Кнопка: " + TextFormatting.GOLD + (macro.getKey() + 100));
                  } else {
                     this.sendMessage(ChatFormatting.WHITE + "Команда: " + TextFormatting.GOLD + macro.getMessage() + ChatFormatting.WHITE + ", Клавиша: " + TextFormatting.GOLD + Keyboard.getKeyName(macro.getKey()));
                  }
               }
            }
         }
      } else {
         this.error();
      }

   }
}
