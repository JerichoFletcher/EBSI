package ebsi.controllers;

import ebsi.core.Env;
import ebsi.net.jda.JDAService;
import ebsi.struct.handlers.MessageHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class PingController implements MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(PingController.class);

    private static final Set<String> names = new HashSet<>();
    static {
        names.add("halo");
        names.add("hai");
        names.add("hello");
        names.add("hi");
        names.add("ping");
    }

    @Override
    public Set<String> getNames() {
        return names;
    }

    @Override
    public <T extends GenericMessageEvent> void handle(T event, String[] eventArgs) {
        if (event instanceof MessageReceivedEvent) {
            LocalDateTime now = LocalDateTime.now();
            Duration onlineTime = Duration.between(Env.onlineStartTime, now);
            String onlineTimeStr = createDurationString(onlineTime);

            MessageEmbed embed = new EmbedBuilder()
                    .setColor(Color.GREEN)
                    .setThumbnail(JDAService.instance().getSelfUser().getAvatarUrl())
                    .setTitle("Hai!")
                    .setDescription("Aku sekarang online!")
                    .addField("Online sejak", Env.onlineStartTime.format(DateTimeFormatter.ofPattern("hh:mm:ss dd/MM/yyyy")), true)
                    .addField("\u200B", "\u200B", true)
                    .addField("Online selama", onlineTimeStr, true)
                    .setTimestamp(now.atZone(ZoneId.systemDefault()))
                    .build();

            event.getChannel().sendMessageEmbeds(embed).submit()
                    .whenComplete((v, e) -> {
                        if (e == null) {
                            logger.info("Sent a ping embed to [{} > #{}]", event.getGuild().getName(), event.getChannel().getName());
                        } else {
                            logger.error("Error while trying to send a ping embed", e);
                        }
                    });
        }
    }

    private String createDurationString(Duration duration) {
        StringBuilder str = new StringBuilder();
        long
                days = duration.toDaysPart(),
                hours = duration.toHoursPart(),
                minutes = duration.toMinutesPart(),
                seconds = duration.toSecondsPart();

        if (days > 0) str.append(String.format("%s hari ", days));
        if (hours > 0) str.append(String.format("%s jam ", hours));
        if (minutes > 0) str.append(String.format("%s menit ", minutes));
        if (days == 0 && hours == 0) str.append(String.format("%s detik", seconds));

        return str.toString();
    }
}
