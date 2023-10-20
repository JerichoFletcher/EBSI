package ebsi_tictactoe.controllers;

import ebsi.core.Coins;
import ebsi.core.UserData;
import ebsi.net.jda.JDAService;
import ebsi.util.Log;
import ebsi_ai.search.game.Minimax;
import ebsi_ai.struct.IntVector;
import ebsi_tictactoe.struct.Board;
import ebsi_tictactoe.struct.BoardEvaluator;
import ebsi_tictactoe.struct.Mark;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.requests.restaction.interactions.MessageEditCallbackAction;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;

import java.util.*;

public class TicTacToeButtonHandler extends ListenerAdapter {
    private final Message gameMessage;
    private final Map<Mark, User> players;
    private final Board board;
    private final boolean versusBot;
    private final Minimax<IntVector, Board> minimax;

    public TicTacToeButtonHandler(Message gameMessage, User player1, User player2) {
        this.gameMessage = gameMessage;

        players = new HashMap<>();
        players.put(Mark.X, player1);
        if (player2 != null) {
            players.put(Mark.O, player2);
            versusBot = false;
        } else {
            players.put(Mark.O, JDAService.instance().getSelfUser());
            versusBot = true;
        }

        minimax = versusBot ? new Minimax<>(new BoardEvaluator()) : null;
        board = new Board();
    }

    @Override
    public void onMessageDelete(MessageDeleteEvent event) {
        // Remove event listener if the message gets deleted
        if (event.getMessageId().equals(gameMessage.getId()))
            JDAService.removeEventListener(this);
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
                event.deferEdit().queue();
                if (!event.getUser().equals(players.get(currentPlayer.adversary())) && Math.random() < 0.1) {
                    event.reply(String.format("TAU GA GAIS @everyone, SI %s SAMA %s LAGI MAIN TICTACTOE EH TAU2NYA SI %s SOK IYE MENCET TOMBOL GAIS",
                                    players.get(Mark.X),
                                    players.get(Mark.O),
                                    event.getUser()))
                            .queue();
                }
                return;
            }

            // Parse button row and column
            String arg = id.replace("ebsi_tictactoe.board.", "");
            int pos = Integer.parseInt(arg);
            int row = pos / 10, col = pos % 10;
            Log.get(this).info("ID: {}, Press: {} {}", id, row, col);

            // Play out the move on the board
            board.act(new IntVector(2, row - 1, col - 1));
            Optional<Mark> winner = board.winner();

            if (!versusBot) {
                if (winner.isEmpty()) {
                    // Game is still ongoing
                    event.editMessage(MessageEditBuilder.fromMessage(event.getMessage())
                            .setContent(String.format("""
                                            Sekarang giliran %s!
                                            Tekan salah satu tombol di bawah untuk meletakkan %s.""",
                                    players.get(board.currentPlayer()),
                                    board.currentPlayer().asEmoji().getFormatted()
                            ))
                            .setComponents(renderBoard(event.getMessage().getActionRows(), false))
                            .build()).queue();
                } else {
                    MessageEditCallbackAction edit;
                    if (winner.get() == Mark.EMPTY) {
                        // Game is tied
                        edit = event.editMessage(String.format("%s dan %s seri! Kamu mendapatkan 5 :coin:!",
                                players.get(Mark.X),
                                players.get(Mark.O)
                        ));
                        Coins.add(event.getGuild(), players.get(Mark.X), 5);
                        Coins.add(event.getGuild(), players.get(Mark.O), 5);
                    } else {
                        // Game is won
                        edit = event.editMessage(String.format("%s menang! Kamu mendapatkan 10 :coin:!",
                                players.get(winner.get())
                        ));
                        Coins.add(event.getGuild(), players.get(winner.get()), 10);
                    }
                    // Render the final board
                    edit.setComponents(renderBoard(event.getMessage().getActionRows(), true)).queue();
                    JDAService.removeEventListener(this);
                }
            } else {
                if (winner.isPresent()) {
                    MessageEditCallbackAction edit;
                    if (winner.get() == Mark.EMPTY) {
                        // Game is tied
                        edit = event.editMessage(String.format("Kita seri, %s! Nih, 2 :coin: buat kamu!",
                                players.get(Mark.X)
                        ));
                        Coins.add(event.getGuild(), players.get(Mark.X), 2);
                    } else {
                        if (winner.get() == Mark.X) {
                            // Human player won
                            edit = event.editMessage(String.format("Selamat, %s! Kamu mendapatkan 5 :coin:!",
                                    players.get(winner.get())
                            ));
                            Coins.add(event.getGuild(), players.get(Mark.X), 5);
                        } else {
                            // Bot won
                            edit = event.editMessage("HAHAHAHAHAHA BISA MAIN TICTACTOE GA SIH");
                        }
                    }
                    // Render the final board
                    edit.setComponents(renderBoard(event.getMessage().getActionRows(), true)).queue();
                    JDAService.removeEventListener(this);
                } else {
                    // Play out the bot move on the board
                    board.act(minimax.search(board).getAction());
                    winner = board.winner();

                    if (winner.isEmpty()) {
                        // Game is still ongoing
                        event.editMessage(MessageEditBuilder.fromMessage(event.getMessage())
                                .setContent(String.format("""
                                            Sekarang giliran %s!
                                            Tekan salah satu tombol di bawah untuk meletakkan %s.""",
                                        players.get(board.currentPlayer()),
                                        board.currentPlayer().asEmoji().getFormatted()
                                ))
                                .setComponents(renderBoard(event.getMessage().getActionRows(), false))
                                .build()).queue();
                    } else {
                        MessageEditCallbackAction edit;
                        if (winner.get() == Mark.EMPTY) {
                            // Game is tied
                            edit = event.editMessage(String.format("Kita seri, %s! Nih, 2 :coin: buat kamu!",
                                    players.get(Mark.X)
                            ));
                            Coins.add(event.getGuild(), players.get(Mark.X), 2);
                        } else {
                            if (winner.get() == Mark.X) {
                                // Human player won
                                edit = event.editMessage(String.format("Selamat, %s! Kamu mendapatkan 5 :coin:!",
                                        players.get(winner.get())
                                ));
                                Coins.add(event.getGuild(), players.get(Mark.X), 5);
                            } else {
                                // Bot won
                                edit = event.editMessage("HAHAHAHAHAHA BISA MAIN TICTACTOE GA SIH");
                            }
                        }
                        // Render the final board
                        edit.setComponents(renderBoard(event.getMessage().getActionRows(), true)).queue();
                        JDAService.removeEventListener(this);
                    }
                }
            }
        }
    }

    private List<ActionRow> renderBoard(List<ActionRow> actionRows, boolean disabled) {
        return actionRows.stream()
                .map(actionRow -> ActionRow.of(actionRow.getButtons().stream()
                        .map(button -> {
                            String arg = button.getId().replace("ebsi_tictactoe.board.", "");
                            int pos = Integer.parseInt(arg);
                            int row = pos / 10, col = pos % 10;

                            Mark mark = board.get(row - 1, col - 1);
                            if (!mark.isEmpty()) {
                                return button.withEmoji(mark.asEmoji()).asDisabled();
                            }
                            return disabled ? button.asDisabled() : button.asEnabled();
                        }).toList())
                ).toList();
    }
}
