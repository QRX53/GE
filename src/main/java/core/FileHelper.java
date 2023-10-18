package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileHelper {

    public static void writeToFile(String content) {
        String filePath = "src/main/resources/alive.csv";

        if (!new File(filePath).exists()) {
            try {
                new File(filePath).createNewFile();
            } catch (IOException e) {
                System.err.println(e.getLocalizedMessage());
                System.exit(-1);
            }
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
            writer.write(content);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}