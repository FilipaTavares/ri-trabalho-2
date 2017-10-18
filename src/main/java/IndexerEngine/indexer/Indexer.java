package IndexerEngine.indexer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;

/**
 * This class indexes a document in a data structure composed of an hashmap in which a key is a term and the value
 * a list of Postings
 *
 * @see Posting
 */

public class Indexer {
    private Map<String, List<Posting>> invertedIndex;

    /**
     * Creates a new instance of Indexer
     */
    public Indexer() {
        this.invertedIndex = new HashMap<>();
    }

    /**
     * Indexes a document given its list of terms and id
     *
     * @param terms list of terms of a document
     * @param docID document id
     */
    public void index(List<String> terms, int docID) {

        for (String term : terms) {
            if (!invertedIndex.containsKey(term)) {

                List<Posting> postingList = new LinkedList<>();
                postingList.add(new Posting(docID, 1));
                invertedIndex.put(term, postingList);

            } else {

                List<Posting> list = invertedIndex.get(term);
                Posting lastPosting = list.get(list.size() - 1);

                if (lastPosting.getDocID() != docID) {
                    list.add(new Posting(docID, 1));
                } else {
                    lastPosting.incrementTermFrequency();
                }
            }
        }
    }

    public void addToIndex(String term, List<Posting> postings) {
        invertedIndex.put(term, postings);
    }

    public List<Posting> getTermPostings(String term) {
        return invertedIndex.get(term);
    }

    /**
     * Returns the number of key-value mappings of the IndexerEngine.indexer
     *
     * @return the size of the IndexerEngine.indexer (the number of key-value mappings)
     */
    public int size() {
        return invertedIndex.size();
    }

    /**
     * String representation of this Indexer Object
     *
     * @return a String representation of this Indexer Object
     */
    @Override
    public String toString() {
        return invertedIndex.toString();
    }

    /**
     * Save the resulting index to a file using the following format (one term per line): term,doc id:term freq, ...
     *
     * @param filename output file name
     */
    public void saveToFile(String filename) {
        try {

            OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(filename),
                    "UTF-8");
            BufferedWriter writer = new BufferedWriter(streamWriter);

            List<String> orderedKeys = invertedIndex.keySet().stream().sorted().collect(Collectors.toList());

            for (String key : orderedKeys) {

                StringBuilder builder = new StringBuilder(key).append(" ");

                Collections.sort(invertedIndex.get(key));

                invertedIndex.get(key).forEach(posting ->
                        builder.append(posting.toString()).append(","));

                String postingList = builder.toString();
                postingList = postingList.substring(0, postingList.length() - 1);
                writer.write(postingList);
                writer.newLine();
            }
            writer.close();

        } catch (IOException e) {
            System.err.println("Unable to save index to file" + e);
        }
    }

    /**
     * Lists the ten first terms (in alphabetic order) that appear in only one document
     *
     * @return a list of the ten first terms (in alphabetic order) that appear in only one document
     */
    public List<String> getFirst10TermsInOneDoc() {
        List<String> terms = invertedIndex.keySet().stream().sorted()
                .filter((term) -> invertedIndex.get(term).size() == 1)
                .collect(Collectors.toList());
        return (this.size() < 10) ? terms.subList(0, this.size()) : terms.subList(0, 10);
    }

    /**
     * Lists the ten terms with higher document frequency
     *
     * @return a list of the ten terms with higher document frequency
     */
    public List<String> getFirst10TermsWithHigherDocFreq() {
        List<String> terms = invertedIndex.keySet().stream().
                sorted(comparingInt(term -> invertedIndex.get(term).size()).reversed())
                .collect(Collectors.toList());
        return (this.size() < 10) ? terms.subList(0, this.size()) : terms.subList(0, 10);
    }
}
