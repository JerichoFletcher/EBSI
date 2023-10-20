package ebsi_ai.struct;

import org.jetbrains.annotations.NotNull;

public class SearchResult<TAction> implements Comparable<SearchResult<TAction>> {
    private final TAction action;
    private final float evaluation;

    public SearchResult(TAction action, float evaluation) {
        this.action = action;
        this.evaluation = evaluation;
    }

    public TAction getAction() {
        return action;
    }

    public float getEvaluation() {
        return evaluation;
    }

    @Override
    public int compareTo(@NotNull SearchResult<TAction> other) {
        return Float.compare(evaluation, other.evaluation);
    }
}
