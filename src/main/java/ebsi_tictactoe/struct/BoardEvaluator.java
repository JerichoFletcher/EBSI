package ebsi_tictactoe.struct;

import ebsi_ai.struct.IGameStateEvaluator;

public class BoardEvaluator implements IGameStateEvaluator<Board> {
    @Override
    public float evaluate(Board state) {
        if (state.winner().isPresent()) {
            if (state.winner().get() == Mark.X) return -100f - state.getActions().size() * 10f;
            if (state.winner().get() == Mark.O) return 100f - state.getActions().size() * 10f;
        }
        return 0f;
    }
}
