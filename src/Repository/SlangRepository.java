package Repository;

import utils.*;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class SlangRepository {
    private Map<String, List<String>> slangMap;
    private Map<String, List<String>> definitionMap;

    // Build slang repository in constructor
    public SlangRepository() {
        ensureWorkingFileExists();

        this.slangMap = FileUtils.loadSlangData(Constants.WORKING_SLANG_FILE);

        buildDefinitionMap();
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

        for (Map.Entry<String, List<String>> entry : this.slangMap.entrySet()) {
            String slang = entry.getKey();

            for (String definition : entry.getValue()) {
                String[] keywords = definition.toLowerCase().split(" ");
                for (String keyword : keywords) {
                    keyword = keyword.replaceAll("[,.?'!]", "");

                    if (keyword.isEmpty()) {
                        continue;
                    }

                    // Build the definition map with the keywords are keys, slang words are values
                    definitionMap.putIfAbsent(slang, new ArrayList<>());
                    if (!definitionMap.get(keyword).contains(slang)) {
                        definitionMap.get(keyword).add(slang);
                    }
                }
            }
        }
    }
}
