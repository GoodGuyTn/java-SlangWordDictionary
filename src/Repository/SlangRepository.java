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

    private List<String> history;

    // Build slang repository in constructor
    private SlangRepository() {
        ensureWorkingFileExists();

        this.slangMap = FileUtils.loadSlangData(Constants.WORKING_SLANG_FILE);
        this.history = FileUtils.loadHistory(Constants.HISTORY_FILE);

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

    public List<String> getHistory() {
        return history;
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

    private void applyChange() {
        buildDefinitionMap();
        saveSlangData();
    }

    private void addToHistory(String slang) {
        history.remove(slang);
        history.addFirst(slang);
        FileUtils.saveHistory(Constants.HISTORY_FILE, this.history);
    }

    private void putSlang(SlangWord slangWord) {
        this.slangMap.put(slangWord.getSlang(), slangWord);
    }

    private void saveSlangData() {
        FileUtils.saveSlangData(Constants.WORKING_SLANG_FILE, this.slangMap);
    }

    public SlangWord findBySlang(String slang) {
        SlangWord slangWord = this.slangMap.get(slang);
        if (slangWord == null) {
            return null;
        }
        addToHistory(slangWord.getSlang());
        return slangWord;
    }

    public Set<SlangWord> findByDefinition(String keywordset) {
        String[] keywords = keywordset.toLowerCase().split(" ");

        // Use HashSet to avoid duplication
        Set<SlangWord> slangWords = new HashSet<>();

        for (String keyword : keywords) {
            keyword = keyword.replaceAll("[,.?'!]", "");
            if (keyword.isEmpty()) {
                continue;
            }

            Set<SlangWord> foundWords = this.definitionMap.get(keyword);
            if (foundWords != null) {
                slangWords.addAll(foundWords);
            }
        }
        return slangWords;
    }

    // Add a slang word
    // True: overwrite | False: duplicate (add new definitions)
    public void addSlang(SlangWord slangWord, boolean overwrite) {
        String slang = slangWord.getSlang();
        List<String> definitions = slangWord.getDefinitions();
        if(!this.slangMap.containsKey(slang) || overwrite) {
            // Case 1: New slang word
            // Case 2: overwrite
            putSlang(slangWord);
        } else {
            List<String> existingDefs = this.slangMap.get(slang).getDefinitions();
            for (String existingDef : existingDefs) {
                slangWord.addDefinition(existingDef);
            }
            putSlang(slangWord);
        }

        applyChange();
    }

    public boolean deleteSlang(String slang) {
        if (!slangMap.containsKey(slang)) {
            return false;
        }
        slangMap.remove(slang);
        applyChange();
        return true;
    }

    public boolean editSlang(String slang, String OldDefinition, String NewDefinition) {
        SlangWord slangWord = findBySlang(slang);
        if (slangWord == null) {
            return false;
        }

        List<String> definitions = slangWord.getDefinitions();
        for (int i = 0; i < definitions.size(); i++) {
            if (definitions.get(i).equals(OldDefinition)) {
                definitions.set(i, NewDefinition);
                applyChange();
                return true;
            }
        }

        return false;
    }

    public boolean removeDefinition(String slang, String definition) {
        SlangWord slangWord = findBySlang(slang);
        if (slangWord == null) {
            return false;
        }

        List<String> definitions = slangWord.getDefinitions();
        for (int i = 0; i < definitions.size(); i++) {
            if (definitions.get(i).equals(definition)) {
                definitions.remove(i);
                applyChange();
                return true;
            }
        }

        return false;
    }

    public void resetSlangData() {
        try {
            Files.copy(Paths.get(Constants.ORIGINAL_SLANG_FILE),
                    Paths.get(Constants.WORKING_SLANG_FILE),
                    StandardCopyOption.REPLACE_EXISTING);

            this.slangMap = FileUtils.loadSlangData(Constants.WORKING_SLANG_FILE);

            buildDefinitionMap();

        } catch (IOException e) {
            System.err.println("Error when resetting slang data!");
            e.printStackTrace();
        }
    }
}
