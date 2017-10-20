package SearchEngine.ScoringAlgorithms;

import IndexerEngine.indexer.Posting;
import SearchEngine.QueryProcessing.Query;

import java.util.List;

public class NumberOfQueryWords implements ScoringAlgorithm {

    @Override
    public Query computeScores(int query_id, List<Posting> postings) {
        Query query = new Query(query_id);
        postings.forEach(posting ->  query.increaseDocScore(posting.getDocID(), 1));

        return query;
    }
}
