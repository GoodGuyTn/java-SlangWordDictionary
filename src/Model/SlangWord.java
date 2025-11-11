package Model;

import java.util.*;

public class SlangWord {
    private String slang;
    private List<String> definitions;

    // Constructor
    public SlangWord() {
        this.definitions = new ArrayList<>();
    }

    public SlangWord(String slang, List<String> definitions) {
        this.slang = slang;
        this.definitions = definitions;
    }

    public SlangWord(String slang, String OnlyDefinition) {
        this.slang = slang;
        this.definitions = new ArrayList<>();
        this.definitions.add(OnlyDefinition);
    }

    // Getter and Setter
    public String getSlang() {
        return slang;
    }

    public void setSlang(String slang) {
        this.slang = slang;
    }

    public List<String> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<String> definitions) {
        this.definitions = definitions;
    }

    // Add new definition
    public void addDefinition(String newDefinition) {
        this.definitions.add(newDefinition);
    }
}
