package ebsi_tictactoe;

import ebsi.net.jda.JDAService;
import ebsi.struct.Command;
import ebsi.util.EmbedTemplate;
import ebsi_tictactoe.controllers.TicTacToeButtonHandler;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EBSITicTacToeCommand extends Command {
    private static final Emoji
            EMOJI_X = Emoji.fromFormatted("U+274C"),
            EMOJI_O = Emoji.fromFormatted("U+2B55");

    private static final Set<String> tags = new HashSet<>();

    static {
        tags.add("tictactoe");
        tags.add("ttt");
    }

    @Override
    public String getName() {
        return "Tic-Tac-Toe";
    }

    @Override
    public String getDescription() {
        return "Memulai permainan Tic-Tac-Toe!";
    }

    @Override
    public String[] getUsages() {
        return new String[]{
                "",
                "<lawan>"
        };
    }

    @Override
    public Set<String> getTags() {
        return tags;
    }

    @Override
    public String getPrimaryTag() {
        return "tictactoe";
    }

    @Override
    public <T1 extends GenericMessageEvent> void handle(T1 event, String[] eventArgs) {
        if (event instanceof MessageReceivedEvent msgEvent) {
            if (eventArgs.length == 0) {
                MessageCreateData message = new MessageCreateBuilder()
                        .setContent(String.format("""
                                    Sekarang giliran <@%s>!
                                    Tekan salah satu tombol di bawah untuk meletakkan :x:.""",
                                msgEvent.getAuthor().getId())
                        )
                        .addActionRow(
                                Button.secondary("ebsi_tictactoe.board.11", "\u200E"),
                                Button.secondary("ebsi_tictactoe.board.12", "\u200E"),
                                Button.secondary("ebsi_tictactoe.board.13", "\u200E")
                        )
                        .addActionRow(
                                Button.secondary("ebsi_tictactoe.board.21", "\u200E"),
                                Button.secondary("ebsi_tictactoe.board.22", "\u200E"),
                                Button.secondary("ebsi_tictactoe.board.23", "\u200E")
                        )
                        .addActionRow(
                                Button.secondary("ebsi_tictactoe.board.31", "\u200E"),
                                Button.secondary("ebsi_tictactoe.board.32", "\u200E"),
                                Button.secondary("ebsi_tictactoe.board.33", "\u200E")
                        )
                        .build();
                JDAService.sendMessage(this, message, event.getChannel(), event.getGuild())
                        .whenComplete((msg, e) -> JDAService.addEventListener(new TicTacToeButtonHandler(msg, msgEvent.getAuthor(), null)));
            } else if (eventArgs.length == 1) {
                List<User> m = msgEvent.getMessage().getMentions().getUsers();
                if (m.size() == 1) {
                    // Prevent self-mention
                    if (m.get(0).equals(msgEvent.getAuthor())) {
                        MessageEmbed embed = EmbedTemplate.errorGeneric("Tic-Tac-Toe!")
                                .setDescription("Ga boleh main lawan diri sendiri ya dek :face_with_hand_over_mouth:")
                                .build();
                        JDAService.sendEmbed(this, embed, event.getChannel(), event.getGuild());
                        return;
                    }

                    MessageCreateData message = new MessageCreateBuilder()
                            .setContent(String.format("""
                                    Sekarang giliran <@%s>!
                                    Tekan salah satu tombol di bawah untuk meletakkan :x:.""",
                                    msgEvent.getAuthor().getId())
                            )
                            .addActionRow(
                                    Button.secondary("ebsi_tictactoe.board.11", "\u200E"),
                                    Button.secondary("ebsi_tictactoe.board.12", "\u200E"),
                                    Button.secondary("ebsi_tictactoe.board.13", "\u200E")
                            )
                            .addActionRow(
                                    Button.secondary("ebsi_tictactoe.board.21", "\u200E"),
                                    Button.secondary("ebsi_tictactoe.board.22", "\u200E"),
                                    Button.secondary("ebsi_tictactoe.board.23", "\u200E")
                            )
                            .addActionRow(
                                    Button.secondary("ebsi_tictactoe.board.31", "\u200E"),
                                    Button.secondary("ebsi_tictactoe.board.32", "\u200E"),
                                    Button.secondary("ebsi_tictactoe.board.33", "\u200E")
                            )
                            .build();
                    JDAService.sendMessage(this, message, event.getChannel(), event.getGuild())
                            .whenComplete((msg, e) -> JDAService.addEventListener(new TicTacToeButtonHandler(msg, msgEvent.getAuthor(), m.get(0))));
                } else {
                    MessageEmbed embed = EmbedTemplate.errorGeneric("Tic-Tac-Toe!")
                            .setDescription("Mention orangnya satu dong aelah ribet amat")
                            .build();
                    JDAService.sendEmbed(this, embed, event.getChannel(), event.getGuild());
                }
            } else {
                MessageEmbed embed = EmbedTemplate.errorArgc(getCommandUsages(), "Tic-Tac-Toe!").build();
                JDAService.sendEmbed(this, embed, event.getChannel(), event.getGuild());
            }
        }
    }
}
