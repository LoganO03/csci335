package learning.markov;

import learning.core.Histogram;

import java.util.*;

public class MarkovChain<L,S> {
    private LinkedHashMap<L, HashMap<Optional<S>, Histogram<S>>> label2symbol2symbol = new LinkedHashMap<>();

    public Set<L> allLabels() {return label2symbol2symbol.keySet();}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (L language: label2symbol2symbol.keySet()) {
            sb.append(language);
            sb.append('\n');
            for (Map.Entry<Optional<S>, Histogram<S>> entry: label2symbol2symbol.get(language).entrySet()) {
                sb.append("    ");
                sb.append(entry.getKey());
                sb.append(":");
                sb.append(entry.getValue().toString());
                sb.append('\n');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    // Increase the count for the transition from prev to next.
    // Should pass SimpleMarkovTest.testCreateChains().
    public void count(Optional<S> prev, L label, S next) {
        // TODO: YOUR CODE HERE
        if(!label2symbol2symbol.containsKey(label)){
            HashMap<Optional<S>, Histogram<S>> newHash = new HashMap<>();
            label2symbol2symbol.put(label,newHash);
        }
        if(!label2symbol2symbol.get(label).containsKey(prev)){
            Histogram<S> newHis = new Histogram<>();
            label2symbol2symbol.get(label).put(prev,newHis);
        }
        label2symbol2symbol.get(label).get(prev).bump(next);

    }

    // Returns P(sequence | label)
    // Should pass SimpleMarkovTest.testSourceProbabilities() and MajorMarkovTest.phraseTest()
    //
    // HINT: Be sure to add 1 to both the numerator and denominator when finding the probability of a
    // transition. This helps avoid sending the probability to zero.
    public double probability(ArrayList<S> sequence, L label) {
        // TODO: YOUR CODE HERE
        double prob = 1;
        Optional<S> prev = Optional.empty();
        for (int i = 0; i < sequence.size(); i++){
            if(i > 0){
                prev = Optional.of(sequence.get(i-1));
            }
            if(label2symbol2symbol.get(label).containsKey(prev)){
                double numerator = label2symbol2symbol.get(label).get(prev).getCountFor(sequence.get(i)) + 1;
                double denominator = label2symbol2symbol.get(label).get(prev).getTotalCounts() + 1;
                prob *= numerator / denominator;
            }
        }
        return prob;
    }

    // Return a map from each label to P(label | sequence).
    // Should pass MajorMarkovTest.testSentenceDistributions()
    public LinkedHashMap<L,Double> labelDistribution(ArrayList<S> sequence) {
        // TODO: YOUR CODE HERE
        LinkedHashMap<L,Double> newHash = new LinkedHashMap<>();
        Set<L> labelList = label2symbol2symbol.keySet();
        double total = 0;
        for(L label: labelList){
            double prob = probability(sequence, label);
            newHash.put(label, prob);
            total += prob;
        }

        for( L label: labelList){
            double prob = newHash.get(label);
            prob /= total;
            newHash.put(label, prob);
        }
        return newHash;
    }

    // Calls labelDistribution(). Returns the label with highest probability.
    // Should pass MajorMarkovTest.bestChainTest()
    public L bestMatchingChain(ArrayList<S> sequence) {
        // TODO: YOUR CODE HERE
        LinkedHashMap<L,Double> hash  = labelDistribution(sequence);
        Map.Entry<L, Double> best = null;
        for(Map.Entry<L, Double> s: hash.entrySet()) {
            if(best == null || s.getValue() > best.getValue()){
                best = s;
            }
        }
        return best.getKey();
    }
}
