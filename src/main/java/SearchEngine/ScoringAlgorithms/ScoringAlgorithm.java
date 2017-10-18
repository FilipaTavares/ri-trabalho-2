package SearchEngine.ScoringAlgorithms;

import IndexerEngine.indexer.Posting;
import SearchEngine.BooleanRetrievals.Query;

import java.util.List;

public interface ScoringAlgorithm {
    Query computeScores(int query_id, List<Posting> postings);
}
