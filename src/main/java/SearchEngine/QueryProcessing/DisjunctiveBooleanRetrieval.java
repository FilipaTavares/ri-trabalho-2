package SearchEngine.QueryProcessing;

import IndexerEngine.indexer.Posting;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DisjunctiveBooleanRetrieval extends BooleanRetrieval {

    @Override
    public void retrieve(int query_id, String query) {
        List<String> terms = tokenizer.tokenize(query);
        List<Posting> allPostings = new ArrayList<>();
        for(String term : terms) {
            if (indexer.getTermPostings(term) != null)
                allPostings.addAll(indexer.getTermPostings(term));
        }
        results.add(scoringAlgorithm.computeScores(query_id, allPostings));
    }

    @Override
    public void saveToFile(String filename) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename)));

            results.sort(Comparator.comparingInt(Query::getQuery_id));

            for (Query query: results) {
                int id = query.getQuery_id();

                query.getDoc_scores().entrySet().stream().sorted((o1, o2) -> o1.getValue().equals(o2.getValue()) ?
                        o1.getKey().compareTo(o2.getKey()) : o2.getValue().compareTo(o1.getValue())).
                        forEach(entry -> out.write(id + " " + entry.getKey()+ " " + entry.getValue() + "\n"));
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}