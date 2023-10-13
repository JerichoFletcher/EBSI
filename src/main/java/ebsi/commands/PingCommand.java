package ebsi.commands;

import ebsi.net.jda.JDAService;
import ebsi.struct.Command;
import ebsi.core.Env;
import ebsi.util.EmbedTemplate;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class PingCommand extends Command {
    private static final Set<String> tags = new HashSet<>();

    static {
        tags.add("ping");
        tags.add("halo");
        tags.add("hai");
        tags.add("hello");
        tags.add("hi");
        tags.add("p");
    }

    @Override
    public String getName() {
        return "Ping";
    }

    @Override
    public String getDescription() {
        return "Melakukan ping ke bot.";
    }

    @Override
    public String[] getUsages() {
        return new String[]{
                ""
        };
    }

    @Override
    public Set<String> getTags() {
        return tags;
    }

    @Override
    public String getPrimaryTag() {
        return "ping";
    }

    @Override
    public <T extends GenericMessageEvent> void handle(T event, String[] eventArgs) {
        if (event instanceof MessageReceivedEvent) {
            LocalDateTime now = LocalDateTime.now();
            Duration onlineTime = Duration.between(Env.onlineStartTime, now);
            String onlineTimeStr = createDurationString(onlineTime);

            MessageEmbed embed = EmbedTemplate.get("Hai!")
                    .setDescription("Aku sekarang online!")
                    .addField("Online sejak", Env.onlineStartTime.format(DateTimeFormatter.ofPattern("hh:mm:ss dd/MM/yyyy")), true)
                    .addBlankField(true)
                    .addField("Online selama", onlineTimeStr, true)
                    .build();

            JDAService.sendEmbed(this, embed, event.getChannel(), event.getGuild());
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
