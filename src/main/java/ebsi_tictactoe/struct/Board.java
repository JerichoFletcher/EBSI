package ebsi_tictactoe.struct;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class Board {
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

    private Mark toPlay;

    public Board() {
        stateMap = 0;
        fillMap = 0;
        toPlay = Mark.X;
    }

    public void play(int row, int col) {
        // Set and flip turn
        set(row, col, toPlay);
        toPlay = toPlay.adversary();
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
        return Mark.from(fillMap & SQUARE_MASK[row * 3 + col], stateMap & SQUARE_MASK[row * 3 + col]);
    }

    public void set(int row, int col, Mark mark) {
        // Construct the square mask for the new mark
        int isNotEmpty = !mark.isEmpty() ? 0xFFF : 0;
        int isO = mark.isO() ? 0xFFF : 0;

        int notEmptyMask = isNotEmpty & SQUARE_MASK[row * 3 + col];
        int oMask = isO & SQUARE_MASK[row * 3 + col];

        // Set the appropriate flags for each bitmap
        fillMap = (short) (fillMap & ~SQUARE_MASK[row * 3 + col] | notEmptyMask);
        stateMap = (short) (stateMap & ~SQUARE_MASK[row * 3 + col] | oMask);
    }
}
