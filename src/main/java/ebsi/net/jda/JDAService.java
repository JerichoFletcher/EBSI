package ebsi.net.jda;

import ebsi.core.Env;
import ebsi.net.jda.listeners.JDAMessageListener;
import ebsi.net.jda.listeners.JDAStatusListener;
import ebsi.util.Log;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.concurrent.CompletableFuture;

public class JDAService {
    private static JDA jda = null;

    public static JDA instance() {
        return jda;
    }

    public static void start() throws InterruptedException {
        if (jda != null) throw new IllegalStateException("JDA has already started");

        jda = JDABuilder.createDefault(Env.getEbsiToken())
                .disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
                .enableIntents(
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                        GatewayIntent.SCHEDULED_EVENTS
                )
                .setBulkDeleteSplittingEnabled(false)
                .addEventListeners(new JDAStatusListener())
                .addEventListeners(new JDAMessageListener())
                .build();
        jda.awaitReady();
    }

    public static CompletableFuture<Message> sendEmbed(Object sender, MessageEmbed embed, MessageChannelUnion channel, Guild guild) {
        return channel.sendMessageEmbeds(embed).submit()
                .whenComplete((msg, e) -> {
                    if (e == null) {
                        Log.get(sender).info("Sent a ping embed to [{} > #{}]",
                                guild == null ? "?" : guild.getName(),
                                channel.getName()
                        );
                    } else {
                        Log.get(sender).error("Error while trying to send a ping embed", e);
                    }
                });
    }
}
