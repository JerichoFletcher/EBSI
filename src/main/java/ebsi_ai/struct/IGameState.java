package ebsi_ai.struct;

import java.util.List;

public interface IGameState<TAction> {
    boolean isTerminal();
    List<TAction> getActions();
    TAction getLastAction();
    IGameState<TAction> getChildFrom(TAction action);
    List<IGameState<TAction>> getChildrenStates();
}
