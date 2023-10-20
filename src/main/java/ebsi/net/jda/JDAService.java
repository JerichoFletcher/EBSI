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
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

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

    public static void addEventListener(Object... adapter) {
        jda.addEventListener(adapter);
        for (Object a : adapter) {
            Log.get(JDAService.class).info("Added event listener '{}'", a.getClass().getSimpleName());
        }
    }

    public static void removeEventListener(Object... adapter) {
        jda.removeEventListener(adapter);
        for (Object a : adapter) {
            Log.get(JDAService.class).info("Removed event listener '{}'", a.getClass().getSimpleName());
        }
    }

    public static CompletableFuture<Message> sendMessage(Object sender, CharSequence msgContent, MessageChannelUnion channel, Guild guild) {
        return sendMessage(sender, new MessageCreateBuilder().addContent(msgContent.toString()).build(), channel, guild);
    }

    public static CompletableFuture<Message> sendEmbed(Object sender, MessageEmbed embed, MessageChannelUnion channel, Guild guild) {
        return sendMessage(sender, new MessageCreateBuilder().addEmbeds(embed).build(), channel, guild);
    }

    public static CompletableFuture<Message> sendMessage(Object sender, MessageCreateData msgData, MessageChannelUnion channel, Guild guild) {
        return channel.sendMessage(msgData).submit()
                .whenComplete((msg, e) -> {
                    if (e == null) {
                        Log.get(sender).info("Sent a message to [{} > #{}]",
                                guild.getName(), channel.getName()
                        );
                    } else {
                        Log.get(sender).error("Error while trying to send a message", e);
                    }
                });
    }

    public static CompletableFuture<Message> replyMessage(Object sender, CharSequence msgContent, Message source) {
        return replyMessage(sender, new MessageCreateBuilder().addContent(msgContent.toString()).build(), source);
    }

    public static CompletableFuture<Message> replyEmbed(Object sender, MessageEmbed embed, Message source) {
        return replyMessage(sender, new MessageCreateBuilder().addEmbeds(embed).build(), source);
    }

    public static CompletableFuture<Message> replyMessage(Object sender, MessageCreateData msgData, Message source) {
        return source.reply(msgData).submit()
                .whenComplete((msg, e) -> {
                    if (e == null) {
                        Log.get(sender).info("Sent a message to [{} > #{}]",
                                source.getGuild().getName(), source.getChannel().getName()
                        );
                    } else {
                        Log.get(sender).error("Error while trying to send a message", e);
                    }
                });
    }
}
