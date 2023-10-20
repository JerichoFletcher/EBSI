package ebsi_tictactoe.struct;

import ebsi_ai.struct.IGameStateEvaluator;

public class BoardEvaluator implements IGameStateEvaluator<Board> {
    @Override
    public float evaluate(Board state) {
        if (state.winner().isPresent()) {
            if (state.winner().get() == Mark.X) return Float.POSITIVE_INFINITY;
            if (state.winner().get() == Mark.O) return Float.NEGATIVE_INFINITY;
        }
        return 0f;
    }
}
