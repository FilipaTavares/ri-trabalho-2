import IndexerEngine.tokenizers.Tokenizer;
import SearchEngine.BooleanRetrievals.BooleanRetrieval;
import SearchEngine.BooleanRetrievals.BooleanRetrievalOR;
import SearchEngine.IndexReader.IndexReader;
import IndexerEngine.indexer.Indexer;
import IndexerEngine.tokenizers.SimpleTokenizer;
import SearchEngine.ScoringAlgorithms.FrequencyOfQueryWords;
import SearchEngine.ScoringAlgorithms.NumberOfQueryWords;
import SearchEngine.ScoringAlgorithms.ScoringAlgorithm;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DocumentSearcher {
    public static void main(String[] args) {

        //falta args e talvez documentSearcher pipeline dependendo de como se fazem os args
        long start = System.currentTimeMillis();

        ArgumentParser parser = ArgumentParsers.newFor("DocumentSearcher").build()
                .defaultHelp(true).description("A simple searcher that uses an index file.");

        parser.addArgument("index_file").type(Arguments.fileType().verifyIsFile()).help("Index file");
        parser.addArgument("queries_file").type(Arguments.fileType().verifyIsFile()).help("Queries file");
        parser.addArgument("scoring_algorithm").choices("qwNumber", "qwFrequency", "all").setDefault("all")
        .help("The scoring algorithm to be used");

        parser.addArgument("outputFile").help("Output file to save results.\nIf scoring algorithm argument" +
        " is set to 'all' then output filenames will be named as follow: scoringclassname_outputFile, " +
                "scoringclassname_outputFile...");

        Namespace ns = parser.parseArgsOrFail(args);

        String index_file = ns.getString("index_file");

        IndexReader indexReader = new IndexReader();
        Indexer indexer = indexReader.readIndex(index_file);

        String tokenizerClassName = indexReader.getTokenizerName();
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

        System.out.println(indexer.size());

        String queries_file = ns.getString("queries_file");
        String scoring_algorithm = ns.getString("scoring_algorithm");
        String output_file = ns.getString("outputFile");

        BooleanRetrieval booleanRetrieval = new BooleanRetrievalOR();
        booleanRetrieval.setIndexer(indexer);
        booleanRetrieval.setTokenizer(tokenizer);

        ScoringAlgorithm scoringAlgorithm;
        switch(scoring_algorithm){
            case "qwNumber":
                scoringAlgorithm = new NumberOfQueryWords();
                booleanRetrieval.setScoringAlgorithm(scoringAlgorithm);
                executeWithOneScoring(booleanRetrieval, queries_file, output_file);
                break;

            case "qwFrequency":
                scoringAlgorithm = new FrequencyOfQueryWords();
                booleanRetrieval.setScoringAlgorithm(scoringAlgorithm);
                executeWithOneScoring(booleanRetrieval, queries_file, output_file);
                break;

            case "all":
                executeWithAllScoring(booleanRetrieval, queries_file, output_file);
                break;
            default:
                break;
        }
        //Criar interface QueryReader??
        //Alterar

        long elapsedTime = System.currentTimeMillis() - start;
        System.out.println("Execution time in ms: " + elapsedTime);
    }

    public static void executeWithOneScoring(BooleanRetrieval booleanRetrieval, String queries_file,
                                             String outputFile) {
        try {
            final int[] query_id = {1};

            Files.lines(Paths.get(queries_file)).forEach(line ->
                    booleanRetrieval.retrieve(query_id[0]++, line));
            booleanRetrieval.saveToFile(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void executeWithAllScoring(BooleanRetrieval booleanRetrieval, String queries_file,
                                             String outputFile) {
        List<String> queries = null;
        try {
            queries = Files.readAllLines(Paths.get(queries_file));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        List<ScoringAlgorithm> scorings = new ArrayList<>();
        scorings.add(new NumberOfQueryWords());
        scorings.add(new FrequencyOfQueryWords());

        for (ScoringAlgorithm scoring : scorings) {
            int query_id = 1;
            booleanRetrieval.setScoringAlgorithm(scoring);
            for (String query: queries){
                booleanRetrieval.retrieve(query_id++, query);
            }
            booleanRetrieval.saveToFile(scoring.getClass().getSimpleName() + "_" + outputFile);
            booleanRetrieval.reset();
        }
    }
}

