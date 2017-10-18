import IndexerEngine.corpusReaders.CorpusReader;
import IndexerEngine.corpusReaders.CranfieldReader;
import IndexerEngine.indexer.Indexer;
import IndexerEngine.tokenizers.Tokenizer;
import Pipelines.DocumentIndexerPipeline;

import java.io.File;
import java.io.IOException;

/**
 * <h2>Document IndexerEngine.indexer</h2>
 * Receives as arguments:
 * <p>The directory containing the IndexerEngine.documents</p>
 * <p>The tokenizer class name to be used</p>
 * <p>The output file name to store de index results</p>
 *
 * @author Ana Filipa Tavares 76629
 * @author Andreia Machado 76501
 */


public class DocumentIndexer {
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();

        if (args.length != 3) {
            System.err.println("REQUIRED ARGUMENTS: <directoryForFiles> <tokenizerClassName> " +
                    "<outputFile>");
            System.exit(1);
        }

        CorpusReader corpusReader = new CranfieldReader();
        Indexer indexer = new Indexer();
        File directory = new File(args[0]);

        if (!(directory.exists() && directory.isDirectory())) {
            System.err.println("No such directory " + args[0]);
            System.exit(1);
        }

        String tokenizerClassName = args[1];
        Tokenizer tokenizer = null;
        Class tokenizerClass;

        try {
            tokenizerClass = Class.forName(Tokenizer.class.getPackage().getName() + "." + tokenizerClassName);
            tokenizer = (Tokenizer) tokenizerClass.newInstance();

        } catch (ClassNotFoundException e) {
            System.err.println("The tokenizer " + tokenizerClassName + " doesn't exist.");
            System.exit(1);
        } catch (IllegalAccessException | InstantiationException e) {
            System.err.println("Tokenizer instantiation failed " + tokenizerClassName + e);
            System.exit(1);
        }

        DocumentIndexerPipeline indexerPipeline = new DocumentIndexerPipeline(directory, corpusReader, tokenizer,
                indexer, args[2]);

        indexerPipeline.execute();

        long elapsedTime = System.currentTimeMillis() - start;
        System.out.println("Execution time in ms: " + elapsedTime);
    }
}