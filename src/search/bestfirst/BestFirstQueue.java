package search.bestfirst;

import search.SearchNode;
import search.SearchQueue;

import java.util.*;
import java.util.function.ToIntFunction;

public class BestFirstQueue<T> implements SearchQueue<T> {
    // TODO: Implement this class
    // HINT: Use java.util.PriorityQueue. It will really help you.
    private Comparator<SearchNode<T>> comparator = new Comparator<SearchNode<T>>() {
        @Override
        public int compare(SearchNode<T> o1, SearchNode<T> o2) {
            int calc0 = o1.getDepth() + heuristic.applyAsInt(o1.getValue());
            int calc1 = o2.getDepth() + heuristic.applyAsInt(o2.getValue());
            if (calc0 < calc1) {
                return -1;
            }
            if (calc0 > calc1) {
                return 1;
            }
            return 0;
        }
    };
    private PriorityQueue<SearchNode<T>> queue = new PriorityQueue<>(comparator);
    private HashMap<T, Integer> visited = new HashMap<>();
    private ToIntFunction<T> heuristic;

    public BestFirstQueue(ToIntFunction<T> heuristic) {this.heuristic = heuristic;}

    @Override
    public void enqueue(SearchNode<T> node) {
        // TODO: Your code here
        int calc = heuristic.applyAsInt(node.getValue()) + node.getDepth();
        if(!visited.containsKey(node.getValue()) || visited.get(node.getValue()) > calc){
            visited.put(node.getValue(), calc);
            queue.add(node);
        }
    }

    @Override
    public Optional<SearchNode<T>> dequeue() {
        // TODO: Your code here
        if (queue.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(queue.remove());
        }

    }
}
