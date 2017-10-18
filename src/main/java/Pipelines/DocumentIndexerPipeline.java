package Pipelines;

import IndexerEngine.corpusReaders.CorpusReader;
import IndexerEngine.documents.Document;
import IndexerEngine.indexer.Indexer;
import IndexerEngine.tokenizers.Tokenizer;

import java.io.File;
import java.util.List;

public class DocumentIndexerPipeline implements Pipeline{
    private File directory;
    private CorpusReader corpusReader;
    private Tokenizer tokenizer;
    private Indexer indexer;
    private String outputFileName;

    public DocumentIndexerPipeline(File directory, CorpusReader corpusReader, Tokenizer tokenizer, Indexer indexer,
                                   String outputFileName) {
        this.directory = directory;
        this.corpusReader = corpusReader;
        this.tokenizer = tokenizer;
        this.indexer = indexer;
        this.outputFileName = outputFileName;
    }

    @Override
    public void execute() {
        File[] fList = directory.listFiles(File::isFile);

        for (File file : fList) {
            Document document = corpusReader.read(file.toString());

            if (document != null) {
                List<String> tokens = tokenizer.tokenize(document.getText());
                indexer.index(tokens, document.getId());
            }
        }

        indexer.saveToFile(outputFileName, tokenizer.getClass().getSimpleName());
        System.out.println("Indexer size: " + indexer.size() + "\n");

        System.out.println("List of ten first terms that appear in only one document");
        List<String> termsInOneDoc = indexer.getFirst10TermsInOneDoc();
        termsInOneDoc.forEach(System.out::println);
        System.out.println();

        System.out.println("List of ten first terms with higher document frequency");

        List<String> termsHigherDocFreq = indexer.getFirst10TermsWithHigherDocFreq();
        termsHigherDocFreq.forEach(System.out::println);
        System.out.println();
    }
}
