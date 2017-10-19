package SearchEngine.ScoringAlgorithms;

import IndexerEngine.indexer.Posting;
import SearchEngine.BooleanRetrievals.Query;

import java.util.List;

public class FrequencyOfQueryWords implements ScoringAlgorithm {

    @Override
    public Query computeScores(int query_id, List<Posting> postings) {
        Query query = new Query(query_id);
        postings.forEach(posting ->  query.increaseDocScore(posting.getDocID(), posting.getTermFreq()) );

        return query;
    }
}
