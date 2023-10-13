package ebsi.commands;

import ebsi.core.Env;
import ebsi.net.jda.JDAService;
import ebsi.struct.Command;
import ebsi.core.Handlers;
import ebsi.util.EmbedTemplate;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HelpCommand extends Command {
    private static final Set<String> tags = new HashSet<>();

    static {
        tags.add("help");
    }

    @Override
    public String getName() {
        return "Help";
    }

    @Override
    public String getDescription() {
        return "Menampilkan informasi *command* yang tersedia.";
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
        return tags;
    }

    @Override
    public String getPrimaryTag() {
        return "help";
    }

    @Override
    public <T1 extends GenericMessageEvent> void handle(T1 event, String[] eventArgs) {
        if (event instanceof MessageReceivedEvent) {
            MessageEmbed embed;
            if (eventArgs.length == 0) {
                List<Command> commands = Handlers.getCommands();
                EmbedBuilder embedBuilder = EmbedTemplate.get("Help!")
                        .setDescription("Berikut adalah list semua *command* yang tersedia:\n\u200E");

                for (Command command : commands) {
                    embedBuilder.addField(
                            String.format("`%s` - %s", command.getPrimaryTag(), command.getName()),
                            String.format("%s\n%s\n\u200E", command.getDescription(), command.getCommandAliases()),
                            false
                    );
                }

                embed = embedBuilder.build();
            } else if (eventArgs.length == 1) {
                Command command = Handlers.getCommands().stream()
                        .filter(c -> c.getTags().contains(eventArgs[0].toLowerCase()))
                        .findFirst().orElse(null);

                if (command == null) {
                    embed = EmbedTemplate.errorGeneric("Help!")
                            .setDescription(String.format(
                                    "Maaf, namun tidak ada *command* dengan nama tersebut.\nLihat daftar *command* dengan perintah `%s%s`.",
                                    Env.PREFIX, getPrimaryTag()
                            )).build();
                } else {
                    embed = EmbedTemplate.get(String.format("`%s` - %s", command.getPrimaryTag(), command.getName()))
                            .setDescription(String.format("%s\n%s\n%s", command.getDescription(), command.getCommandAliases(), command.getCommandUsages()))
                            .build();
                }
            } else {
                embed = EmbedTemplate.errorArgc(getCommandUsages(), "Help!").build();
            }
            JDAService.sendEmbed(this, embed, event.getChannel(), event.getGuild());
        }
    }
}
