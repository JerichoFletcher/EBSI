package ebsi.commands;

import ebsi.core.Env;
import ebsi.net.jda.JDAService;
import ebsi.struct.Command;
import ebsi.core.Handlers;
import ebsi.util.EmbedTemplate;
import ebsi.util.Log;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HelpCommand extends Command {
    private static final Set<String> names = new HashSet<>();
    static {
        names.add("help");
    }

    @Override
    public String getName() {
        return "Help";
    }

    @Override
    public String getDescription() {
        return "Menampilkan semua *command* yang tersedia, atau menampilkan informasi untuk sebuah *command*.";
    }

    @Override
    public String[] getUsages() {
        return new String[]{
                "",
                "<nama command>"
        };
    }

    @Override
    public Set<String> getTags() {
        return names;
    }

    @Override
    public <T1 extends GenericMessageEvent> void handle(T1 event, String[] eventArgs) {
        MessageEmbed embed;
        if (eventArgs.length == 0) {
            List<Command> commands = Handlers.getCommands();
            EmbedBuilder embedBuilder = EmbedTemplate.get()
                    .setTitle("Help!")
                    .setDescription("Berikut adalah list semua *command* yang tersedia:\n\u200E");

            for (Command command : commands) {
                embedBuilder.addField(
                        String.format("`%s` - %s", command.getPrimaryTag(), command.getName()),
                        String.format("%s\n%s\n\u200E", command.getDescription(), getCommandAliases(command)),
                        false
                );
            }

            embed = embedBuilder.build();
        } else if (eventArgs.length == 1) {
            Command command = Handlers.getCommands().stream()
                    .filter(c -> c.getTags().contains(eventArgs[0].toLowerCase()))
                    .findFirst().orElse(null);

            if (command == null) {
                embed = EmbedTemplate.error()
                        .setTitle("Help!")
                        .setDescription(String.format(
                                "Maaf, namun tidak ada *command* dengan nama tersebut.\nLihat daftar *command* dengan perintah `%s%s`.",
                                Env.PREFIX, getPrimaryTag()
                        )).build();
            } else {
                embed = EmbedTemplate.get()
                        .setTitle(String.format("`%s` - %s", command.getPrimaryTag(), command.getName()))
                        .setDescription(String.format("%s\n%s\n%s", command.getDescription(), getCommandAliases(command), getCommandUsages(command)))
                        .build();
            }
        } else {
            embed = EmbedTemplate.error()
                    .setTitle("Help!")
                    .setDescription(getCommandUsages(this))
                    .build();
        }
        JDAService.sendEmbed(this, embed, event.getChannel(), event.getGuild());
    }

    private static String getCommandAliases(Command command) {
        return String.format("*Alias:* `%s`", String.join("`, `", command.getTags()));
    }

    private static String getCommandUsages(Command command) {
        StringBuilder str = new StringBuilder();
        str.append("*Usages:*");

        String primaryTag = command.getPrimaryTag();
        for (String usage : command.getUsages()) {
            str.append(String.format("\n`%s%s", Env.PREFIX, primaryTag));
            if (!usage.isBlank()) str.append(String.format(" %s", usage));
            str.append("`");
        }

        return str.toString();
    }
}
