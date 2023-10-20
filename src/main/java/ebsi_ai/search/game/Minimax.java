package ebsi_ai.search.game;

import ebsi.util.Log;
import ebsi_ai.struct.IGameState;
import ebsi_ai.struct.IGameStateEvaluator;
import ebsi_ai.struct.SearchResult;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class Minimax<TAction, TState extends IGameState<TAction>> {
    private final IGameStateEvaluator<TState> evaluator;

    public Minimax(IGameStateEvaluator<TState> evaluator) {
        this.evaluator = evaluator;
    }

    public SearchResult<TAction> search(TState initialState) {
        List<TState> children = (List<TState>) initialState.getChildrenStates();
        float bestResult = Float.NEGATIVE_INFINITY;
        List<TAction> bestActions = new ArrayList<>();
        for (TState child : children) {
            float result = search(child, bestResult, Float.NEGATIVE_INFINITY, true);
            Log.get(this).info("Principal move: {} with eval {}", child.getLastAction(), result);
            if (bestResult < result) {
                bestResult = result;
                bestActions.clear();
            }
            if (bestResult == result) {
                bestActions.add(child.getLastAction());
            }
        }

        SearchResult<TAction> result = new SearchResult<>(bestActions.get((int) (Math.random() * bestActions.size())), bestResult);
        Log.get(this).info("Result move is: {} with evaluation {}", result.getAction(), result.getEvaluation());
        return result;
    }

    private float search(IGameState<TAction> state, float alpha, float beta, boolean isMaximizing) {
        if (state.isTerminal()) {
            float eval = evaluator.evaluate((TState) state);
            Log.get(this).info("Board:\n{}Evaluation: {}", state, eval);
            return eval;
        }

        List<TState> children = (List<TState>) state.getChildrenStates();
        float bestResult;
//        Log.get(this).info("Board:\n{}Children count: {}", state, children.size());
        if (isMaximizing) {
            bestResult = Float.NEGATIVE_INFINITY;
            for (TState child : children) {
                Log.get(this).info("    Checking move {}", child.getLastAction().toString());
                // Recursively call minimax on child
                float newEval = search(child, alpha, beta, false);
                if (bestResult < newEval) bestResult = newEval;

                // Prune check
                alpha = Math.max(alpha, newEval);
                if (alpha >= beta) break;
            }
        } else {
            bestResult = Float.POSITIVE_INFINITY;
            for (TState child : children) {
                Log.get(this).info("    Checking move {}", child.getLastAction().toString());
                // Recursively call minimax on child
                float newEval = search(child, alpha, beta, true);
                if (bestResult > newEval) bestResult = newEval;

                // Prune check
                beta = Math.min(alpha, newEval);
                if (alpha >= beta) break;
            }
        }
        return bestResult;
    }
}
