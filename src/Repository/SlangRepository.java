package Repository;

import utils.*;
import Model.*;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class SlangRepository {
    private static SlangRepository instance;

    private Map<String, SlangWord> slangMap;
    private Map<String, Set<SlangWord>> definitionMap;

    // Build slang repository in constructor
    private SlangRepository() {
        ensureWorkingFileExists();

        this.slangMap = FileUtils.loadSlangData(Constants.WORKING_SLANG_FILE);

        buildDefinitionMap();
    }

    public static SlangRepository getInstance() {
        if (instance == null) {
            instance = new SlangRepository();
        }
        return instance;
    }

    // Getter
    public Map<String, SlangWord> getSlangMap() {
        return slangMap;
    }

    public Map<String, Set<SlangWord>> getDefinitionMap() {
        return definitionMap;
    }

    private void ensureWorkingFileExists() {
        try {
            if (!Files.exists(Paths.get(Constants.WORKING_SLANG_FILE))) {
                Files.copy(Paths.get(Constants.ORIGINAL_SLANG_FILE),
                            Paths.get(Constants.WORKING_SLANG_FILE),
                            StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            System.err.println("Error when creating working files!");
            e.printStackTrace();
        }
    }

    private void buildDefinitionMap() {
        this.definitionMap = new HashMap<>();

        for (Map.Entry<String, SlangWord> entry : this.slangMap.entrySet()) {
            SlangWord slangWord = entry.getValue();

            for (String definition : entry.getValue().getDefinitions()) {
                String[] keywords = definition.toLowerCase().split(" ");
                for (String keyword : keywords) {
                    keyword = keyword.replaceAll("[,.?'!]", "");

                    if (keyword.isEmpty()) {
                        continue;
                    }

                    // Build the definition map with the keywords are keys, slang words are values
                    definitionMap.putIfAbsent(keyword, new HashSet<>());
                    definitionMap.get(keyword).add(slangWord);
                }
            }
        }
    }
}
