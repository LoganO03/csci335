package learning.sentiment.learners;

import learning.classifiers.Knn;
import learning.core.Histogram;

public class Knn9 extends Knn<Histogram<String>,String>  {
    public Knn9() {
        super(9, Histogram::cosineDistance);
    }
}
