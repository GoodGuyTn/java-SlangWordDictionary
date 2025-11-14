package Service;

import Repository.*;
import Model.*;
import java.util.*;

public class SlangService {
    private SlangRepository repository;

    public SlangService() {
        this.repository = SlangRepository.getInstance();
    }

    // Feature 1: Search by slang
    public SlangWord searchBySlang(String slang) {
        return this.repository.findBySlang(slang);
    }

    // Feature 2: Search by definition
    public Set<SlangWord> searchByDefinition(String definition) {
        return this.repository.findByDefinition(definition);
    }

    // Feature 3: Get slang history
    public  List<String> getSlangHistory() {
        return this.repository.getHistory();
    }

    // Feature 4: Add a slang word
    public void addSlangWord(String slang, String definition) {
        // Default case: duplicate
        SlangWord slangWord = new SlangWord(slang, definition);
        this.repository.addSlang(slangWord, false);
    }

    public void addSlangWord(String slang, String definition, boolean overwrite) {
        SlangWord slangWord = new SlangWord(slang, definition);
        this.repository.addSlang(slangWord, overwrite);
    }

    // Feature 5: Edit a slang word by replace an old definition with a new definition
    public void editSlangWord(String slang, String OldDefinition, String NewDefinition) {
        SlangWord slangWord = this.repository.findBySlang(slang);
        if (slangWord == null) {
            return;
        }

        List<String> definitions = slangWord.getDefinitions();
        for(int i = 0; i < definitions.size(); i++) {
            if (definitions.get(i).equals(OldDefinition)) {
                definitions.set(i, NewDefinition);
                break;
            }
        }
    }

    // Feature 6: Delete a slang word
    public void deleteSlangWord(String slang) {
        this.repository.deleteSlang(slang);
    }

    // Feature 7: Reset to original slang data
    public void resetOriginalSlangData() {
        this.repository.resetSlangData();
    }

    // Feature 8: Random slang word
    public SlangWord getRandomSlangWord() {
        List<SlangWord> allSlangWords = new ArrayList<>(this.repository.getSlangMap().values());
        if (allSlangWords.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int index = random.nextInt(allSlangWords.size());
        return allSlangWords.get(index);
    }

    // This function gets the 4 option for Quiz features
    // Output is a list of slang word with the first one (index 0) is the correct option
    public List<SlangWord> getQuizOptions() {
        List<SlangWord> options = new ArrayList<>();

        SlangWord correctOption = getRandomSlangWord();
        if (correctOption == null) {
            return null;
        }
        options.add(correctOption);

        // Gets other 3 slang word for wrong options
        Set<SlangWord> wrongOptions = new HashSet<>();
        while(wrongOptions.size() < 3) {
            SlangWord wrongOption = getRandomSlangWord();
            if (!wrongOption.getSlang().equals(correctOption.getSlang())) {
                wrongOptions.add(wrongOption);
            }
        }
        options.addAll(wrongOptions);

        return options;
    }

}
