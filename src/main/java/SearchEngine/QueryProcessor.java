package SearchEngine;

import SearchEngine.BooleanRetrievals.BooleanRetrieval;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class QueryProcessor {

    public void processQueries(String queries_file, BooleanRetrieval booleanRetrieval, String outputFile){

        int query_id = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(queries_file))) {

            String line;
            while ((line = br.readLine()) != null) {
                booleanRetrieval.retrieve(query_id++, line);
            }

            br.close();
            booleanRetrieval.saveToFile(outputFile);

        } catch (IOException e) {
            System.err.println("Error reading queries file " + queries_file);
            System.exit(1);
        }

    }
}
