package Service;

import Repository.*;
import Model.*;
import java.util.*;

public class SlangService {
    private SlangRepository repository;

    public SlangService() {
        this.repository = SlangRepository.getInstance();
    }

    // Feature1: Find by slang word
    public SlangWord findBySlang(String slang) {
        return this.repository.getSlangMap().get(slang);
    }

    // Feature2: Find by definition
    public Set<SlangWord> findByDefinition(String keywordset) {
        String[] keywords = keywordset.toLowerCase().split(" ");

        // Use HashSet to avoid duplication
        Set<SlangWord> slangWords = new HashSet<>();

        for (String keyword : keywords) {
            keyword = keyword.replaceAll("[,.?'!]", "");
            if (keyword.isEmpty()) {
                continue;
            }

            Set<SlangWord> foundWords = this.repository.getDefinitionMap().get(keyword);
            if (foundWords != null) {
                slangWords.addAll(foundWords);
            }
        }
        return slangWords;
    }
}
