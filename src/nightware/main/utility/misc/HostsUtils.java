package nightware.main.utility.misc;

import java.io.*;

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

    public static void removeLineFromFile(String lineToRemove) {
        try {
            File file = new File("C:/Windows/System32/drivers/etc/hosts");

            if (!file.isFile()) {
                System.out.println("File not exists.");
                return;
            }

            File tempFile = new File(file.getAbsolutePath() + ".tmp");

            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;
            boolean found = false;

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.trim().equals(lineToRemove)) {
                    found = true;
                    continue;
                }
                writer.write(currentLine + System.getProperty("line.separator"));
            }

            writer.close();
            reader.close();

            if (!found) {
                System.out.println("String not found.");
                return;
            }

            if (!file.delete()) {
                System.out.println("File not found.");
                return;
            }

            if (!tempFile.renameTo(file))
                System.out.println("Temp file not be renamed.");

            System.out.println("String succefull deleted from file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
