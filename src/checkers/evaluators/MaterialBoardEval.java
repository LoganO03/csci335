package checkers.evaluators;

import checkers.core.Checkerboard;
import checkers.core.PlayerColor;

import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;

public class MaterialBoardEval implements ToIntFunction<Checkerboard>  {
    @Override
    public int applyAsInt(Checkerboard value) {
        return value.numPiecesOf(value.getCurrentPlayer()) - value.numPiecesOf(value.getCurrentPlayer().opponent());
    }
}
