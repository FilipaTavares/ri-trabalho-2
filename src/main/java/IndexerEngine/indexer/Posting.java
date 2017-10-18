package IndexerEngine.indexer;

/**
 * This class represents regarding a given term a tuple (docID, termFreq) which states that the term is present
 * in the document with the id docID and it appears termFreq times
 */

public class Posting implements Comparable<Posting> {
    private int docID;
    private int termFreq;

    /**
     * Creates a new instance of Posting
     *
     * @param docID    the document id
     * @param termFreq the term frequency
     */
    public Posting(int docID, int termFreq) {
        this.docID = docID;
        this.termFreq = termFreq;
    }

    /**
     * Returns the document id
     *
     * @return the document id
     */
    public int getDocID() {
        return docID;
    }

    /**
     * Return the term frequency in the document
     *
     * @return the term frequency in the document
     */
    public int getTermFreq() {
        return termFreq;
    }

    /**
     * Increments the term frequency
     */
    public void incrementTermFrequency() {
        this.termFreq++;
    }

    /**
     * String object representation of this Posting
     *
     * @return a String object representing of this Posting.
     */

    @Override
    public String toString() {
        return docID + ":" + termFreq;
    }

    /**
     * Compares two Posting objects numerically according to the document id
     *
     * @param anotherPosting the Posting to be compared
     * @return the value 0 if this Posting is equal to the argument Posting; a value less than 0 if this Posting is
     * numerically less than the argument Posting; and a value greater
     * than 0 if this Posting is numerically greater than the argument Posting (signed comparison).
     */
    @Override
    public int compareTo(Posting anotherPosting) {
        return Integer.compare(this.docID, anotherPosting.getDocID());
    }
}
