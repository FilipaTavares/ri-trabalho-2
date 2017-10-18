package IndexerEngine.tokenizers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple tokenizer implementation
 */

public class SimpleTokenizer implements Tokenizer {

    /**
     * Method that splits on whitespace, lowercases tokens, removes all non-alphabetic characters,
     * and keeps only terms with 3 or more characters
     *
     * @param inputText text to be processed
     * @return list of generated tokens
     */
    @Override
    public List<String> tokenize(String inputText) {
        String[] words = inputText.split("[^\\p{Alpha}]+");

        return Arrays.stream(words).filter(word -> word.length() >= 3).map(String::toLowerCase)
                .collect(Collectors.toList());
    }
}
