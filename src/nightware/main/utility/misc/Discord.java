package nightware.main.utility.misc;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import lombok.Getter;
import nightware.main.NightWare;
import ru.crashdami.internalprotection.nativeobfuscator.Native;

@Native
public class Discord {

    @Getter
    private static final String discordID = "1067032653590700093";

    @Getter
    private static final DiscordRichPresence discordRichPresence = new DiscordRichPresence();

    @Getter
    private static final DiscordRPC discordRPC = DiscordRPC.INSTANCE;

    public static void startRPC() {
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);
        Discord.discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        Discord.discordRichPresence.largeImageKey = "nightware";
        new Thread(() -> {
            while (true) {
                try {
                    Discord.discordRichPresence.details = "User: " + NightWare.getInstance().getUserInfo().getName() + " / UID: " + NightWare.getInstance().getUserInfo().getUid();
                    Discord.discordRichPresence.state = NightWare.getInstance().getUserInfo().getRole();
                    discordRPC.Discord_UpdatePresence(discordRichPresence);
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
            }
        }).start();
    }

    public static void stopRPC() {
        discordRPC.Discord_Shutdown();
    }
}

