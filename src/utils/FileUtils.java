package utils;

import Model.*;
import java.io.*;
import java.util.*;

public class FileUtils {
    // Load the original slang.txt
    public static Map<String, SlangWord> loadSlangData(String filePath) {
        Map<String, SlangWord> slangMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            br.readLine();
            while ((line = br.readLine()) != null) {
                // Split slang word and its definitions
                String[] parts = line.split("`");

                if (parts.length == 2) {
                    String slang = parts[0].trim();
                    String definitionsStr = parts[1].trim();

                    // Split definitions
                    String[] definitionsArray = definitionsStr.split("\\|");

                    List<String> definitions = new ArrayList<>();
                    for (String definition : definitionsArray) {
                        definitions.add(definition.trim());
                    }

                    SlangWord slangword = new SlangWord(slang, definitions);

                    slangMap.put(slang, slangword);
                }
            }
        } catch (IOException e) {
            System.err.println("Error when reading file: " + filePath);
            e.printStackTrace();
        }

        return slangMap;
    }

    // Save current HashMap to binary file
    public static void saveObject(String filePath, Object object) {
        // Referenced
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read binary file
    public static Object loadObject(String filePath) {
        // Referenced
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public static List<String> loadHistory(String filePath) {
        List<String> history = new ArrayList<>();
        File file = new File(filePath);

        // Nếu file chưa tồn tại (lần chạy đầu) thì trả về list rỗng
        if (!file.exists()) {
            return history;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    history.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error when reading history: " + filePath);
            e.printStackTrace();
        }
        return history;
    }

    public static void saveHistory(String filePath, List<String> history) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String item : history) {
                bw.write(item + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error when saving history: " + filePath);
            e.printStackTrace();
        }
    }
}
