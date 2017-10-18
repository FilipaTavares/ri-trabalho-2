package SearchEngine.BooleanRetrievals;

import IndexerEngine.indexer.Indexer;
import IndexerEngine.tokenizers.Tokenizer;
import SearchEngine.ScoringAlgorithms.ScoringAlgorithm;

import java.util.*;

public abstract class BooleanRetrieval {
    protected List<Query> results;
    protected Indexer indexer;
    protected Tokenizer tokenizer;
    protected ScoringAlgorithm scoringAlgorithm;

    public BooleanRetrieval() {
        this.results = new LinkedList<>();
    }

    public abstract void retrieve(int query_id, String query);

    public abstract void saveToFile(String filename);

    /*
    public Map<Query, Integer> getResults() {
        return Collections.unmodifiableMap(results);
    }
    */

    public void setIndexer(Indexer indexer) {
        this.indexer = indexer;
    }

    public void setTokenizer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public void setScoringAlgorithm(ScoringAlgorithm scoringAlgorithm) {
        this.scoringAlgorithm = scoringAlgorithm;
    }

    public void reset(){ this.results.clear(); }
}
