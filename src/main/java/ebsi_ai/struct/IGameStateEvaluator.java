package ebsi_ai.struct;

public interface IGameStateEvaluator<T extends IGameState<?>> {
    float evaluate(T state);
}
