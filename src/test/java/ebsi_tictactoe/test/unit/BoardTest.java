package ebsi_tictactoe.test.unit;

import ebsi_tictactoe.struct.Board;
import ebsi_tictactoe.struct.Mark;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    @Test
    public void test_WhenInstantiateBoard_ThenBoardIsEmpty() {
        Board board = new Board();
        for (int i = 0; i < 9; i++)
            assertEquals(Mark.EMPTY, board.get(i / 3, i % 3));
    }

    @Test
    public void test_WhenSetMark_ThenGetCorrectMark() {
        Board board = new Board();
        for (int k = 3; k >= 1; k--) {
            for (int i = 0; i < 9; i++) {
                Mark mark = Mark.from(k);
                board.set(i / 3, i % 3, mark);
                assertEquals(mark, board.get(i / 3, i % 3));
            }
        }
    }
}
