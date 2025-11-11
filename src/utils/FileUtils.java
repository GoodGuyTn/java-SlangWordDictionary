package utils;

import Model.*;
import java.io.*;
import java.util.*;

public class FileUtils {
    // Load the original slang.txt
    public static Map<String, List<String>> loadSlangData(String filePath) {
        Map<String, List<String>> slangMap = new HashMap<>();

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

                    slangMap.put(slang, definitions);
                }
            }
        } catch (IOException e) {
            System.err.println("Error when reading file: " + filePath);
            e.printStackTrace();
        }

        return slangMap;
    }

    // Save current HashMap to file
    public static void saveSlangData(String filePath, Map<String, List<String>> slangMap) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("Slang`Meaning\n");

            for (Map.Entry<String, List<String>> entry : slangMap.entrySet()) {
                String slang = entry.getKey();
                List<String> definitions = entry.getValue();
                String definitionsStr = String.join("\\|", definitions);

                bw.write(slang + "`" + definitionsStr + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error when writing file: " + filePath);
            e.printStackTrace();
        }
    }
}
