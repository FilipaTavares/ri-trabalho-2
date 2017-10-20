package SearchEngine.QueryProcessing;

import java.util.HashMap;
import java.util.Map;

public class Query {
    private int query_id;
    private Map<Integer, Integer> doc_scores;

    public Query(int query_id) {
        this.query_id = query_id;
        doc_scores = new HashMap<>();
    }

    public int getQuery_id() {
        return query_id;
    }

    public Map<Integer, Integer> getDoc_scores() {
        return doc_scores;
    }

    public void increaseDocScore(int docID, int number) {
        doc_scores.put(docID, doc_scores.containsKey(docID) ? doc_scores.get(docID) + number : number);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Query query = (Query) o;

        return query_id == query.query_id;
    }

    @Override
    public int hashCode() {
        return query_id;
    }

    @Override
    public String toString() {
        return "query_id=" + query_id;
    }
}
