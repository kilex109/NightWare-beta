package net.minecraft.client.gui;

import java.awt.*;
import java.io.IOException;
import javax.annotation.Nullable;

import nightware.main.NightWare;
import nightware.main.manager.dragging.DragManager;
import nightware.main.module.impl.movement.Timer;
import nightware.main.module.impl.util.BetterChat;
import nightware.main.module.impl.util.PasswordHider;
import nightware.main.utility.math.Vec2i;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.blur.BlurUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ITabCompleter;
import net.minecraft.util.TabCompleter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import nightware.main.utility.render.font.Fonts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiChat extends GuiScreen implements ITabCompleter
{
    private static final Logger LOGGER = LogManager.getLogger();
    private String historyBuffer = "";

    /**
     * keeps position of which chat message you will select when you press up, (does not increase for duplicated
     * messages sent immediately after each other)
     */
    private int sentHistoryCursor = -1;
    private TabCompleter tabCompleter;

    /** Chat entry field */
    protected GuiTextField inputField;

    /**
     * is the text that appears when you press the chat key and the input box appears pre-filled
     */
    private String defaultInputFieldText = "";

    public GuiChat()
    {
    }

    public GuiChat(String defaultText)
    {
        this.defaultInputFieldText = defaultText;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
        this.inputField = new GuiTextField(0, this.fontRendererObj, 4, this.height - 12, this.width - 4, 12);
        this.inputField.setMaxStringLength(256);
        this.inputField.setEnableBackgroundDrawing(false);
        this.inputField.setFocused(true);
        this.inputField.setText(this.defaultInputFieldText);
        this.inputField.setCanLoseFocus(false);
        this.tabCompleter = new GuiChat.ChatTabCompleter(this.inputField);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        this.mc.ingameGUI.getChatGUI().resetScroll();
        Timer timerModule = NightWare.getInstance().getModuleManager().timer;
        DragManager.getDraggables().values().forEach((dragging) -> {
            if (dragging.getModule().isEnabled()) {
                dragging.onRelease(0);
            }
        });
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        this.inputField.updateCursorCounter();
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        this.tabCompleter.resetRequested();

        if (keyCode == 15)
        {
            this.tabCompleter.complete();
        }
        else
        {
            this.tabCompleter.resetDidComplete();
        }

        if (keyCode == 1)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
        else if (keyCode != 28 && keyCode != 156)
        {
            if (keyCode == 200)
            {
                this.getSentHistory(-1);
            }
            else if (keyCode == 208)
            {
                this.getSentHistory(1);
            }
            else if (keyCode == 201)
            {
                this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().getLineCount() - 1);
            }
            else if (keyCode == 209)
            {
                this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().getLineCount() + 1);
            }
            else
            {
                this.inputField.textboxKeyTyped(typedChar, keyCode);
            }
        }
        else
        {
            String s = this.inputField.getText().trim();

            if (!s.isEmpty())
            {
                this.sendChatMessage(s);
            }

            this.mc.displayGuiScreen((GuiScreen)null);
        }
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0)
        {
            if (i > 1)
            {
                i = 1;
            }

            if (i < -1)
            {
                i = -1;
            }

            if (!isShiftKeyDown())
            {
                i *= 7;
            }

            this.mc.ingameGUI.getChatGUI().scroll(i);
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        if (mouseButton == 0)
        {
            ITextComponent itextcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());

            if (itextcomponent != null && this.handleComponentClick(itextcomponent))
            {
                return;
            }
        }

        this.inputField.mouseClicked(mouseX, mouseY, mouseButton);
        Timer timerModule = NightWare.getInstance().getModuleManager().timer;
        DragManager.getDraggables().values().forEach((dragging) -> {
            if (dragging.getModule().isEnabled()) {
                dragging.onClick(mouseX, mouseY, mouseButton);
            }

        });
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        Timer timerModule = NightWare.getInstance().getModuleManager().timer;
        DragManager.getDraggables().values().forEach((dragging) -> {
            if (dragging.getModule().isEnabled()) {
                dragging.onRelease(state);
            }

        });
        super.mouseReleased(mouseX, mouseY, state);
    }

    /**
     * Sets the text of the chat
     */
    protected void setText(String newChatText, boolean shouldOverwrite)
    {
        if (shouldOverwrite)
        {
            this.inputField.setText(newChatText);
        }
        else
        {
            this.inputField.writeText(newChatText);
        }
    }

    /**
     * input is relative and is applied directly to the sentHistoryCursor so -1 is the previous message, 1 is the next
     * message from the current cursor position
     */
    public void getSentHistory(int msgPos)
    {
        int i = this.sentHistoryCursor + msgPos;
        int j = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
        i = MathHelper.clamp(i, 0, j);

        if (i != this.sentHistoryCursor)
        {
            if (i == j)
            {
                this.sentHistoryCursor = j;
                this.inputField.setText(this.historyBuffer);
            }
            else
            {
                if (this.sentHistoryCursor == j)
                {
                    this.historyBuffer = this.inputField.getText();
                }

                this.inputField.setText((String)this.mc.ingameGUI.getChatGUI().getSentMessages().get(i));
                this.sentHistoryCursor = i;
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        if (NightWare.getInstance().getModuleManager().getModule(BetterChat.class).isEnabled() && BetterChat.changeChat.get()) {
            RenderUtility.drawFixedGlow(2, this.height - 14, this.width - 3, 13, 10, new Color(33, 33, 33, 150).getRGB());
            RenderUtility.drawRoundedRect(2, this.height - 14, this.width - 3, 13, 5, new Color(33, 33, 33, 150).getRGB());
        } else {
            drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
        }
        this.inputField.drawTextBox();
        ITextComponent itextcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());

        if (itextcomponent != null && itextcomponent.getStyle().getHoverEvent() != null)
        {
            this.handleComponentHover(itextcomponent, mouseX, mouseY);
        }

        if (NightWare.getInstance().getModuleManager().getModule(PasswordHider.class).isEnabled() && (this.inputField.getText().startsWith("/l") && this.inputField.getText().split(" ")[0].length() <= 6 || this.inputField.getText().startsWith("/reg") && this.inputField.getText().split(" ")[0].length() <= 9 && this.inputField.getText().split(" ").length != 1)) {
            BlurUtility.drawBlur(10.0F, () -> {
                if (NightWare.getInstance().getModuleManager().getModule(BetterChat.class).isEnabled() && BetterChat.changeChat.get()) {
                    RenderUtility.drawRectNoWH((double) (Fonts.mntsb18.getStringWidth(this.inputField.getText().split(" ")[0]) + 7), this.height - 12, (double) (Fonts.mntsb18.getStringWidth(this.inputField.getText().split(" ")[0]) + 6 + Fonts.mntsb18.getStringWidth(this.inputField.getText().substring(this.inputField.getText().split(" ")[0].length()))), (double) (this.height - 2), Integer.MIN_VALUE);
                } else {
                    RenderUtility.drawRectNoWH((double) (this.mc.fontRendererObj.getStringWidth(this.inputField.getText().split(" ")[0]) + 7), this.height - 12, (double) (this.mc.fontRendererObj.getStringWidth(this.inputField.getText().split(" ")[0]) + 6 + this.mc.fontRendererObj.getStringWidth(this.inputField.getText().substring(this.inputField.getText().split(" ")[0].length()))), (double) (this.height - 2), Integer.MIN_VALUE);
                }
            });
        }

        DragManager.getDraggables().values().forEach((dragging) -> {
            if (dragging.getModule().isEnabled()) {
                dragging.onDraw(mouseX, mouseY);
            }
        });

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Sets the list of tab completions, as long as they were previously requested.
     */
    public void setCompletions(String... newCompletions)
    {
        this.tabCompleter.setCompletions(newCompletions);
    }

    public static class ChatTabCompleter extends TabCompleter
    {
        private final Minecraft clientInstance = Minecraft.getMinecraft();

        public ChatTabCompleter(GuiTextField p_i46749_1_)
        {
            super(p_i46749_1_, false);
        }

        public void complete()
        {
            super.complete();

            if (this.completions.size() > 1)
            {
                StringBuilder stringbuilder = new StringBuilder();

                for (String s : this.completions)
                {
                    if (stringbuilder.length() > 0)
                    {
                        stringbuilder.append(", ");
                    }

                    stringbuilder.append(s);
                }

                this.clientInstance.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(stringbuilder.toString()), 1);
            }
        }

        @Nullable
        public BlockPos getTargetBlockPos()
        {
            BlockPos blockpos = null;

            if (this.clientInstance.objectMouseOver != null && this.clientInstance.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                blockpos = this.clientInstance.objectMouseOver.getBlockPos();
            }

            return blockpos;
        }
    }
}
