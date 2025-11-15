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

    @Override
    public boolean equals(Object o) {
        // 1. Nếu so sánh với chính nó -> Trả về true ngay (nhanh nhất)
        if (this == o) return true;

        // 2. Nếu đối tượng kia là null hoặc khác loại Class -> False
        if (o == null || getClass() != o.getClass()) return false;

        // 3. Ép kiểu đối tượng kia về SlangWord để so sánh
        SlangWord slangWord = (SlangWord) o;

        // 4. So sánh nội dung của thuộc tính 'slang'
        // Dùng Objects.equals để tránh lỗi nếu slang bị null
        return Objects.equals(slang, slangWord.slang);
    }

    @Override
    public int hashCode() {
        // Tạo mã băm dựa trên thuộc tính 'slang'
        return Objects.hash(slang);
    }

    @Override
    public String toString() {
        String allDefinitions = String.join(" | ", this.definitions);
        return this.slang + " : " + allDefinitions;
    }
}
