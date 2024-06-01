import java.util.Map;
import java.util.TreeMap;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;

public class PhoneBook {
    public static void main(String[] args) throws IOException {
        String name, number;
        TreeMap<String, String> phoneBook = new TreeMap<String, String>();
        //File name "phonebook.txt" and folder "telephoneBook" (should be located in local drive "C" by default)
        //It is not necessary to create the "phonebook.txt" file manually
        String data_file_name = "phonebook.txt";
        File dataFile = new File("C:\\telephoneBook\\" + data_file_name);
        try {
            if (!dataFile.exists()) {
                dataFile.createNewFile();
                System.out.println("Folder found: 'telephoneBook'");
                System.out.println("File successfully created: 'phonebook.txt'");
            } else {
                System.out.println("Loading data: phonebook.txt...");
                System.out.println("Data loaded successfully");
                try (Scanner scanner = new Scanner(dataFile)) {
                    while (scanner.hasNextLine()) {
                        String phoneEntry = scanner.nextLine();
                        int separatorPosition = phoneEntry.indexOf('%');
                        if (separatorPosition == -1) throw new IOException("An error occurred in the file format");
                        {
                            name = phoneEntry.substring(0, separatorPosition);
                            number = phoneEntry.substring(separatorPosition + 1);
                            phoneBook.put(name, number);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Missing folder " + '"' + "telephoneBook" + '"');
            System.out.println("File not found: " + '"' + dataFile.getName() + '"');
            System.out.println("File path: " + dataFile.getAbsolutePath());
        }
        Scanner in = new Scanner(System.in);
        boolean changed = false;

        mainLoop:
        while (true) {
            System.out.println("------------------------------------------ -----");
            System.out.println("Select the category you want to execute: ");
            System.out.println("1. Find the phone number");
            System.out.println("2. Add or change phone number");
            System.out.println("3. Deleting an entry from the telephone directory");
            System.out.println("4. Phone directory list");
            System.out.println("0. Exit the program");
            System.out.print("Select categories: ");
            int command;
            if (in.hasNextInt()) {
                command = in.nextInt();
                in.nextLine();
            } else {
                System.out.println("***INCORRECT INPUT***");
                in.nextLine();
                continue;
            }

            switch (command) {
                case 1:
                    System.out.print("Enter the name you want to search for: ");
                    name = in.nextLine().trim();
                    number = phoneBook.get(name);
                    if (number == null) {
                        System.out.println("***Name <" + name + "> does not exist***");
                    } else {
                        System.out.println("***Subscriber name: <" + name + "> with number: <" + number + ">***");
                    }
                    break;
                case 2:
                    System.out.print("Enter name: ");
                    name = in.nextLine().trim();
                    if (name.length() == 0) {
                        System.out.println("***Data cannot be empty***");
                    } else if (name.indexOf('%') >= 0) {
                        System.out.println("***Data must not contain the \"%\"*** character");
                    } else {
                        System.out.print("Enter phone number: ");
                        number = in.nextLine().trim();
                        if (number.length() == 0) {
                            System.out.println("***Data cannot be empty***");
                        } else {
                            phoneBook.put(name, number);
                            changed = true;
                            System.out.println("***Record <" + name + "> with number <" + number + "> added to the directory***");
                        }
                    }
                    break;
                case 3:
                    System.out.print("Enter the name of the subscriber you want to delete: ");
                    name = in.nextLine().trim();
                    number = phoneBook.get(name);
                    if (number == null) {
                        System.out.println("***Missing entry <" + name + "> in the directory***");
                    } else {
                        phoneBook.remove(name);
                        changed = true;
                        System.out.println("***Record " + '"' + name + '"' + " successfully removed from the directory***");
                    }
                    break;
                case 4:
                    System.out.println("***List of entries in the directory***");
                    for (Map.Entry<String, String> entry : phoneBook.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue());
                    }
                    break;
                case 0:
                    System.out.println("***Exit the program***");
                    break mainLoop;
                default:
                    System.out.println("***INCORRECT INPUT***");
            }
        }
        if (changed) {
            System.out.println("Saving data " + dataFile.getAbsolutePath() + "...");
            PrintWriter out;
            try {
                out = new PrintWriter(new FileWriter(dataFile));
            } catch (IOException e) {
                System.out.println("ERROR! Can't save file! File " + '"' + "phonebook.txt" + '"' + " absent");
                return;
            }
            for (Map.Entry<String, String> entry : phoneBook.entrySet())
                out.println(entry.getKey() + "%" + entry.getValue());
            out.flush();
            out.close();
            if (out.checkError()) {
                System.out.println("An error occurred while writing the file");
            } else {
                System.out.println("Change saved successfully");
            }
        }
    }
}