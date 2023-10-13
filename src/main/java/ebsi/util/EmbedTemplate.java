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

    public static EmbedBuilder get(String title) {
        return get().setTitle(title);
    }

    public static EmbedBuilder errorGeneric() {
        return get().setColor(Color.RED);
    }

    public static EmbedBuilder errorGeneric(String title) {
        return get(title).setColor(Color.RED);
    }

    public static EmbedBuilder errorArgc(String usages) {
        return EmbedTemplate.errorGeneric()
                .setDescription(String.format("Gini loh cara pakenya :expressionless:\n%s", usages));
    }

    public static EmbedBuilder errorArgc(String usages, String title) {
        return errorArgc(usages).setTitle(title);
    }
}
