package ebsi.util;

import ebsi.net.jda.JDAService;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class EmbedTemplate {
    public static EmbedBuilder get() {
        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setThumbnail(JDAService.instance().getSelfUser().getAvatarUrl())
                .setTimestamp(LocalDateTime.now().atZone(ZoneId.systemDefault()));
    }

    public static EmbedBuilder error() {
        return get().setColor(Color.RED);
    }
}
