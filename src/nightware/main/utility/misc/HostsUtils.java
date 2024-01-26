package nightware.main.utility.misc;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HostsUtils {
    public static void addHostsEntry(String hostsEntry) {
        try {

            BufferedReader fileReader = new BufferedReader(new FileReader("C:/Windows/System32/drivers/etc/hosts"));
            boolean isEntryExist = false;
            String line;
            while ((line = fileReader.readLine()) != null) {
                if (line.trim().equals(hostsEntry.trim())) {
                    isEntryExist = true;
                    break;
                }
            }
            fileReader.close();
            if (!isEntryExist) {
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter("C:/Windows/System32/drivers/etc/hosts", true));
                fileWriter.newLine();
                fileWriter.write("\n" + hostsEntry);
                fileWriter.close();
                System.out.println("Added.");
            } else {
                System.out.println("Exists.");
            }
        } catch (IOException e) {
            System.out.println("No Access");
        }
    }

    public static void removeLineFromFile(String searchString) {

        List<String> lines = new ArrayList<>();
        File file = new File("C:/Windows/System32/drivers/etc/hosts");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains(searchString)) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
