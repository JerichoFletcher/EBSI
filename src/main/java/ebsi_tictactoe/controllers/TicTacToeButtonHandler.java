package ebsi_tictactoe.controllers;

import ebsi.net.jda.JDAService;
import ebsi_tictactoe.struct.Board;
import ebsi_tictactoe.struct.Mark;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;

import java.util.*;

public class TicTacToeButtonHandler extends ListenerAdapter {
    private final Message gameMessage;
    private final Map<Mark, User> players;
    private final Board board;

    public TicTacToeButtonHandler(Message gameMessage, User player1, User player2) {
        this.gameMessage = gameMessage;

        players = new HashMap<>();
        players.put(Mark.X, player1);
        players.put(Mark.O, player2);

        board = new Board();
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        // Ignore button events from other messages
        if (!event.getMessage().equals(gameMessage)) return;

        String id = event.getButton().getId();
        if (id != null && id.startsWith("ebsi_tictactoe.board.")) {
            // Ignore button presses from non-players
            Mark currentPlayer = board.currentPlayer();
            if (!event.getUser().equals(players.get(currentPlayer))) {
                if (event.getUser().equals(players.get(currentPlayer.adversary()))) {
//                    event.reply(String.format("Sabar loh <@%s>, ini gilirannya <@%s>",
//                                    players.get(currentPlayer.adversary()).getId(),
//                                    players.get(currentPlayer).getId()))
//                            .queue();
                } else {
                    event.reply(String.format("TAU GA GAIS @everyone, SI <@%s> SAMA <@%s> LAGI MAIN TICTACTOE EH TAU2NYA SI <@%s> SOK IYE MENCET TOMBOL GAIS",
                                    players.get(Mark.X).getId(),
                                    players.get(Mark.O).getId(),
                                    event.getUser().getId()))
                            .queue();
                }
                return;
            }

            String arg = id.replace("ebsi_tictactoe.board.", "");
            int pos = Integer.parseInt(arg);
            int row = pos / 10, col = pos % 10;

            board.play(row - 1, col - 1);
            Optional<Mark> winner = board.winner();

            if (winner.isEmpty()) {
                // Game is still ongoing
                event.editMessage(MessageEditBuilder.fromMessage(event.getMessage())
                        .setContent(String.format("""
                                    Sekarang giliran %s!
                                    Tekan salah satu tombol di bawah untuk meletakkan %s.""",
                                players.get(board.currentPlayer()),
                                board.currentPlayer().asEmoji().getFormatted()
                        ))
                        .setComponents(event.getMessage().getActionRows().stream().map(
                                actionRow -> ActionRow.of(actionRow.getButtons().stream().map(button -> {
                                    if (button.equals(event.getButton())) {
                                        return button.withLabel(currentPlayer.asEmoji().getFormatted()).asDisabled();
                                    } else return button;
                                }).toList())
                        ).toList())
                        .build()).queue();
            } else if (winner.get() == Mark.EMPTY) {
                // Game is tied
                event.editMessage(String.format("%s dan %s seri! Kamu mendapatkan 50 :coin:! Tapi boong ea",
                                players.get(Mark.X),
                                players.get(Mark.O)
                        ))
                        .setComponents(event.getMessage().getActionRows().stream()
                                .map(actionRow -> ActionRow.of(actionRow.getButtons().stream().map(button -> {
                                            if (button.equals(event.getButton())) {
                                                return button.withLabel(currentPlayer.asEmoji().getFormatted()).asDisabled();
                                            } else return button.asDisabled();
                                        })
                                        .toList()))
                                .toList())
                        .queue();
                JDAService.removeEventListener(this);
            } else {
                // Game is won
                event.editMessage(String.format("%s menang! Kamu mendapatkan 100 :coin:! Tapi boong ea",
                                players.get(winner.get())
                        ))
                        .setComponents(event.getMessage().getActionRows().stream()
                                .map(actionRow -> ActionRow.of(actionRow.getButtons().stream().map(button -> {
                                            if (button.equals(event.getButton())) {
                                                return button.withLabel(currentPlayer.asEmoji().getFormatted()).asDisabled();
                                            } else return button.asDisabled();
                                        })
                                        .toList()))
                                .toList())
                        .queue();
                JDAService.removeEventListener(this);
            }
        }
    }
}
