package SearchEngine.ScoringAlgorithms;

import IndexerEngine.indexer.Posting;
import SearchEngine.QueryProcessing.Query;

import java.util.List;

public interface ScoringAlgorithm {
    Query computeScores(int query_id, List<Posting> postings);
}
