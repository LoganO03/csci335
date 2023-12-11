package checkers.searchers;

import checkers.core.Checkerboard;
import checkers.core.CheckersSearcher;
import checkers.core.Move;
import core.Duple;

import java.util.Optional;
import java.util.function.ToIntFunction;

public class NegaMax extends CheckersSearcher{
    private int numNodes = 0;
    private int depth = 10;
    public NegaMax(ToIntFunction<Checkerboard> e) {
        super(e);
    }

    @Override
    public int numNodesExpanded() {
        return numNodes;
    }

    public Optional<Duple<Integer, Move>> selectMoveHelp(Checkerboard board, int depth) {
        Optional<Duple<Integer, Move>> best = Optional.empty();
        if(board.gameOver()) {
            if (board.playerWins(board.getCurrentPlayer())) {
                best = Optional.of(new Duple<>(Integer.MAX_VALUE, board.getLastMove()));
            } else {
                best = Optional.of(new Duple<>(-Integer.MAX_VALUE, board.getLastMove()));
            }
        } else if (depth == 0){
            best = Optional.of(new Duple<>(this.getEvaluator().applyAsInt(board),board.getLastMove()));
        }
        else {
            for (Checkerboard alternative : board.getNextBoards()) {
                numNodes += 1;
                int negation = board.getCurrentPlayer() != alternative.getCurrentPlayer() ? -1 : 1;
                Optional<Duple<Integer, Move>> future = selectMoveHelp(alternative,depth - 1);
                int value = future.get().getFirst() * negation;
                if (best.isEmpty() || value > best.get().getFirst()){
                    best = Optional.of(new Duple<>(value, alternative.getLastMove()));
                }
            }
        }
        return best;
    }
    @Override
    public Optional<Duple<Integer, Move>> selectMove(Checkerboard board) {
        return selectMoveHelp(board,getDepthLimit());
    }
}
