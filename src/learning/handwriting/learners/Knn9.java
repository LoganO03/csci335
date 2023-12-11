package learning.handwriting.learners;
import learning.classifiers.Knn;
import learning.classifiers.Knn;
import learning.handwriting.core.Drawing;

public class Knn9 extends Knn<Drawing,String> {
    public Knn9() {
        super(9, (d1, d2) -> (double)d1.distance(d2));
    }
}