import SearchEngine.BooleanRetrievals.BooleanRetrieval;
import SearchEngine.BooleanRetrievals.BooleanRetrievalOR;
import SearchEngine.IndexReader.IndexReader;
import IndexerEngine.indexer.Indexer;
import IndexerEngine.tokenizers.SimpleTokenizer;
import SearchEngine.ScoringAlgorithms.FrequencyOfQueryWords;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DocumentSearcher {
    public static void main(String[] args) {

        //falta args e talvez documentSearcher pipeline dependendo de como se fazem os args

        long start = System.currentTimeMillis();

        IndexReader indexReader = new IndexReader();
        Indexer indexer = indexReader.readIndex("results.txt");

        System.out.println(indexer.size());
        BooleanRetrieval booleanRetrieval = new BooleanRetrievalOR();
        booleanRetrieval.setIndexer(indexer);
        booleanRetrieval.setTokenizer(new SimpleTokenizer());
        booleanRetrieval.setScoringAlgorithm(new FrequencyOfQueryWords());

        //Criar interface QueryReader??
        try {
            final int[] query_id = {1};

            Files.lines(Paths.get("cranfield.queries.txt")).forEach(line ->
                    booleanRetrieval.retrieve(query_id[0]++, line));
            booleanRetrieval.saveToFile("queries_results_with_query.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        long elapsedTime = System.currentTimeMillis() - start;
        System.out.println("Execution time in ms: " + elapsedTime);
    }
}