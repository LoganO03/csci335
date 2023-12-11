package learning.som;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.ToDoubleBiFunction;

public class SelfOrgMap<V> {
    private V[][] map;
    private ToDoubleBiFunction<V, V> distance;
    private WeightedAverager<V> averager;

    public SelfOrgMap(int side, Supplier<V> makeDefault, ToDoubleBiFunction<V, V> distance, WeightedAverager<V> averager) {
        this.distance = distance;
        this.averager = averager;
        map = (V[][])new Object[side][side];
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                map[i][j] = makeDefault.get();
            }
        }
    }

    // TODO: Return a SOMPoint corresponding to the map square which has the
    //  smallest distance compared to example.
    // NOTE: The unit tests assume that the map is traversed in row-major order,
    //  that is, the y-coordinate is updated in the outer loop, and the x-coordinate
    //  is updated in the inner loop.
    public SOMPoint bestFor(V example) {
        // Your code here.
        Double bestDist = Double.MAX_VALUE;
        int x = 0;
        int y = 0;
        for(int i = 0; i < getMapHeight(); i++){
            for(int j = 0; j < getMapWidth(); j++){
                Double currentDist = distance.applyAsDouble(map[j][i], example);
                if(currentDist < bestDist){
                    bestDist = currentDist;
                    y = i;
                    x = j;
                }
            }
        }
        return new SOMPoint(x, y);
    }

    // TODO: Train this SOM with example.
    //  1. Find the best matching node.
    //  2. Update the best matching node with the average of itself and example,
    //     using a learning rate of 0.9.
    //  3. Update each neighbor of the best matching node that is in the map,
    //     using a learning rate of 0.4.
    public void train(V example) {
        // Your code here
        SOMPoint matchNode = bestFor(example);
        V matchingValue = map[matchNode.x()][matchNode.y()];
        map[matchNode.x()][matchNode.y()] = averager.weightedAverage(example, matchingValue, .9);
        SOMPoint[] neighbors = matchNode.neighbors();
        for(SOMPoint neighbor: neighbors){
            if(neighbor.x() < getMapWidth() && neighbor.y() < getMapHeight() && neighbor.y() >= 0 && neighbor.x() >= 0) {
                V neighborValue = map[neighbor.x()][neighbor.y()];
                map[neighbor.x()][neighbor.y()] = averager.weightedAverage(example, neighborValue, 0.4);
            }
        }
    }

    public V getNode(int x, int y) {
        return map[x][y];
    }

    public int getMapWidth() {
        return map.length;
    }

    public int getMapHeight() {
        return map[0].length;
    }

    public int numMapNodes() {
        return getMapHeight() * getMapWidth();
    }

    public boolean inMap(SOMPoint point) {
        return point.x() >= 0 && point.x() < getMapWidth() && point.y() >= 0 && point.y() < getMapHeight();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof SelfOrgMap that) {
            if (this.getMapHeight() == that.getMapHeight() && this.getMapWidth() == that.getMapWidth()) {
                for (int x = 0; x < getMapWidth(); x++) {
                    for (int y = 0; y < getMapHeight(); y++) {
                        if (!map[x][y].equals(that.map[x][y])) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int x = 0; x < getMapWidth(); x++) {
            for (int y = 0; y < getMapHeight(); y++) {
                result.append(String.format("(%d, %d):\n", x, y));
                result.append(getNode(x, y));
            }
        }
        return result.toString();
    }
}