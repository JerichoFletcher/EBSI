package ebsi.commands;

import ebsi.net.jda.JDAService;
import ebsi.struct.Command;
import ebsi.util.EmbedTemplate;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashSet;
import java.util.Set;

public class ThrowDiceCommand extends Command {
    private static final Set<String> tags = new HashSet<>();

    static {
        tags.add("dadu");
        tags.add("dice");
        tags.add("rand");
        tags.add("random");
        tags.add("throw");
        tags.add("lempar");
    }


    @Override
    public String getName() {
        return "Dadu";
    }

    @Override
    public String getDescription() {
        return "Melempar dadu dengan banyak sisi yang diberikan.";
    }

    @Override
    public String[] getUsages() {
        return new String[]{
                "<n>"
        };
    }

    @Override
    public Set<String> getTags() {
        return tags;
    }

    @Override
    public String getPrimaryTag() {
        return "dadu";
    }

    @Override
    public <T1 extends GenericMessageEvent> void handle(T1 event, String[] eventArgs) {
        if (event instanceof MessageReceivedEvent) {
            MessageEmbed embed;
            if (eventArgs.length != 1) {
                embed = EmbedTemplate.errorArgc(getCommandUsages(this), "Dadu!").build();
            } else {
                try {
                    int val = Integer.parseInt(eventArgs[0]);
                    if (val <= 0) throw new NumberFormatException();
                    int r = (int)(Math.random() * val) + 1;

                    embed = EmbedTemplate.get("Dadu!")
                            .setDescription("Nilai dadu kamu adalah:")
                            .addField(String.format(":game_die: %s", Integer.valueOf(r).toString()), "\u200B", false)
                            .build();
                } catch(NumberFormatException ignored) {
                    embed = EmbedTemplate.errorGeneric("Dadu!")
                            .setDescription("Waduh:point_up::sweat_smile:\nBilangan bulat positif aja dong mas/mba")
                            .build();
                }
            }
            JDAService.sendEmbed(this, embed, event.getChannel(), event.getGuild());
        }
    }
}
