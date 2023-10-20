package ebsi_tictactoe.struct;

import ebsi_ai.struct.IGameState;
import ebsi_ai.struct.IntVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Board implements IGameState<IntVector> {
    private static final Short[]
            ROW_MASK = new Short[]{0b111000000, 0b000111000, 0b000000111},
            COL_MASK = new Short[]{0b100100100, 0b010010010, 0b001001001},
            DIAG_MASK = new Short[]{0b100010001, 0b001010100},
            SQUARE_MASK, THREE_MASK;
    static {
        SQUARE_MASK = new Short[9];
        for (int i = 0; i < 9; i++) {
            SQUARE_MASK[i] = (short) (ROW_MASK[i / 3] & COL_MASK[i % 3]);
        }
        THREE_MASK = Stream.concat(Stream.concat(Arrays.stream(ROW_MASK), Arrays.stream(COL_MASK)), Arrays.stream(DIAG_MASK)).toArray(Short[]::new);
    }

    private short stateMap;
    private short fillMap;

    private Mark[] state;

    private Mark toPlay;
    private IntVector lastAction;

    public Board() {
        stateMap = 0;
        fillMap = 0;
        toPlay = Mark.X;
        lastAction = null;

        state = new Mark[9];
        Arrays.fill(state, Mark.EMPTY);
    }

    public Board(Board other) {
        stateMap = other.stateMap;
        fillMap = other.fillMap;
        toPlay = other.toPlay;
        lastAction = other.lastAction;

        state = Arrays.copyOf(other.state, 9);
    }

    @Override
    public IGameState<IntVector> getChildFrom(IntVector action) {
        Board board = new Board(this);
        board.act(action);
        return board;
    }

    public void act(IntVector action) {
        if (action.getDimension() != 2) throw new IllegalArgumentException("Incompatible dimension");

        // Get vector components
        int row = action.getValue(0);
        int col = action.getValue(1);
        if (row > 2 || col > 2) throw new IndexOutOfBoundsException(action.toString());

        // Set and flip turn
        set(row, col, toPlay);
        toPlay = toPlay.adversary();

        // Store this action
        lastAction = action;
    }

    @Override
    public List<IntVector> getActions() {
        List<IntVector> actions = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (state[i] == Mark.EMPTY) actions.add(new IntVector(2, i / 3, i % 3));
        }
        return actions;

//        List<IntVector> actions = new ArrayList<>();
//        int empty = ~fillMap;
//        for (int i = 8; i >= 0; i--) {
//            if ((empty & 1) == 1) actions.add(new IntVector(2, i / 3, i % 3));
//            empty >>>= 1;
//        }
//        return actions;
    }

    @Override
    public IntVector getLastAction() {
        return lastAction;
    }

    @Override
    public List<IGameState<IntVector>> getChildrenStates() {
        List<IGameState<IntVector>> children = new ArrayList<>();
        for (IntVector action : getActions()) {
            Board child = new Board(this);
            child.act(action);
            children.add(child);
        }
        return children;
    }

    @Override
    public boolean isTerminal() {
        return winner().isPresent();
    }

    public Mark currentPlayer() {
        return toPlay;
    }

    public Optional<Mark> winner() {
        // Check each row, column, and diagonal
        for (short mask : THREE_MASK) {
            if ((mask & fillMap) != mask) continue;

            short check = (short) (mask & stateMap);
            if (check == mask) return Optional.of(Mark.O);
            if (check == 0) return Optional.of(Mark.X);
        }

        // Check if is drawn
        return fillMap == 0b111111111 ? Optional.of(Mark.EMPTY) : Optional.empty();
    }

    public Mark get(int row, int col) {
        // Read the flags from stored bitmaps and construct the appropriate mark
        return state[row * 3 + col];
//        return Mark.from(fillMap & SQUARE_MASK[row * 3 + col], stateMap & SQUARE_MASK[row * 3 + col]);
    }

    public void set(int row, int col, Mark mark) {
        state[row * 3 + col] = mark;

        // Construct the square mask for the new mark
        int isNotEmpty = !mark.isEmpty() ? 0xFFF : 0;
        int isO = mark.isO() ? 0xFFF : 0;

        int notEmptyMask = isNotEmpty & SQUARE_MASK[row * 3 + col];
        int oMask = isO & SQUARE_MASK[row * 3 + col];

        // Set the appropriate flags for each bitmap
        fillMap = (short) (fillMap & ~SQUARE_MASK[row * 3 + col] | notEmptyMask);
        stateMap = (short) (stateMap & ~SQUARE_MASK[row * 3 + col] | oMask);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                str.append(switch (state[row * 3 + col]) {
                    case X -> "X";
                    case O -> "O";
                    case EMPTY -> "_";
                });
            }
            str.append("\n");
        }
        return str.append(String.format("Last move: %s\n", lastAction)).toString();
    }
}
