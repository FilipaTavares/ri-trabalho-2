package SearchEngine.IndexReader;

import IndexerEngine.indexer.Indexer;
import IndexerEngine.indexer.Posting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class IndexReader {

    public Indexer readIndex(String filename) {
        Indexer indexer = new Indexer();

        try {
            Files.lines(Paths.get(filename)).forEach((String line) -> {

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
                        continue;
                    }

                    postings.add(new Posting(docId, termFreq));
                }

                indexer.addToIndex(term, postings);
            });


        } catch (IOException e) {
            e.printStackTrace();
        }

        return indexer;
    }

}
