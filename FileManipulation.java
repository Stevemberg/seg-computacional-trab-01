import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class FileManipulation {
    private static final String COMMA_DELIMITER = ";";

    public static String readFile(String name) {
        File file = new File("./textfiles/" + name);
        StringBuilder result = new StringBuilder("");
        try {
            BufferedReader buffer = new BufferedReader(new FileReader(file));
            String r = "";
            while ((r = buffer.readLine()) != null) {
                result.append(r);
            }
            buffer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static String readFile(String name, boolean format) {
        if (format)
            return removeSpecialCharacters(readFile(name));
        return readFile(name);
    }

    public static void writeFile(String name, String text) {
        if (name == null || name.isBlank())
            name = "output.txt";

        File file = new File("./textfiles/" + name);
        try {
            BufferedWriter buffer = new BufferedWriter(new FileWriter(file));
            buffer.write(text);
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String removeSpecialCharacters(String text) {
        StringBuilder result = new StringBuilder("");
        char character = ' ';

        for (int i = 0; i < text.length(); i++) {
            character = text.charAt(i);
            if (character > 96 && character < 123)
                character -= 32;
            else if (character < 65 || character > 90)
                continue;
            result.append(character);
        }
        return result.toString();
    }

    public static HashMap<String, Double> readCSV(String fileName) {
        HashMap<String, Double> result = new HashMap<>();
        String fileContent = readFile(fileName);
        String[] arrayContent = fileContent.split(COMMA_DELIMITER);

        for (int i = 0; i < arrayContent.length; i = i + 2) {
            result.put(arrayContent[i], Double.valueOf(arrayContent[i + 1]));
        }
        return result;
    }
}