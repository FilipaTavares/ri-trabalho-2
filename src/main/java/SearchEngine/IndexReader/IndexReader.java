package SearchEngine.IndexReader;

import IndexerEngine.indexer.Indexer;
import IndexerEngine.indexer.Posting;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class IndexReader {
    private String tokenizerName;

    public String getTokenizerName() {
        return tokenizerName;
    }

    public Indexer readIndex(String filename) {
        Indexer indexer = new Indexer();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)))){
            String line;

            if ((line = reader.readLine()) != null)
                tokenizerName = line.trim();

            while ((line = reader.readLine()) != null) {
                String[] s = line.split("[ ,]");
                String term = s[0];
                List<Posting> postings = new LinkedList<>();

                for (int i = 1; i < s.length; i++) {
                    String[] split = s[i].split(":");
                    int docId;
                    int termFreq;
                    try {
                        docId = Integer.parseInt(split[0]);
                        termFreq = Integer.parseInt(split[1]);
                    } catch (NumberFormatException e) {
                        System.err.println("Error processing posting from file");
                        System.out.println(Arrays.toString(split));
                        continue;
                    }

                    postings.add(new Posting(docId, termFreq));
                }

                indexer.addToIndex(term, postings);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return indexer;
    }
}

