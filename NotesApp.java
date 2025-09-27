import java.io.*;
import java.util.*;

/**
 * NotesApp: A simple text-based notes manager using File I/O.
 * Stores notes in "notes.txt" file, one note per line.
 * Supports adding, viewing, and deleting notes.
 */
public class NotesApp {
    private static final String FILENAME = "notes.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Notes Manager ===");
            System.out.println("1. Add a Note");
            System.out.println("2. View All Notes");
            System.out.println("3. Delete a Note");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addNote(scanner);
                        break;
                    case 2:
                        viewNotes();
                        break;
                    case 3:
                        deleteNote(scanner);
                        break;
                    case 4:
                        System.out.println("Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
        scanner.close();
    }

    /**
     * Adds a new note to the file by appending it.
     */
    private static void addNote(Scanner scanner) {
        System.out.print("Enter your note: ");
        String note = scanner.nextLine().trim();

        if (note.isEmpty()) {
            System.out.println("Note cannot be empty.");
            return;
        }

        try (FileWriter writer = new FileWriter(FILENAME, true)) { // Append mode
            writer.write(note + "\n");
            System.out.println("Note added successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Reads and displays all notes from the file, numbered.
     */
    private static void viewNotes() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            List<String> notes = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    notes.add(line);
                }
            }

            if (notes.isEmpty()) {
                System.out.println("No notes found.");
                return;
            }

            System.out.println("\nYour Notes:");
            for (int i = 0; i < notes.size(); i++) {
                System.out.println((i + 1) + ". " + notes.get(i));
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            // Create empty file if it doesn't exist
            try (FileWriter writer = new FileWriter(FILENAME)) {
                // Empty file
            } catch (IOException ex) {
                System.out.println("Error creating file: " + ex.getMessage());
            }
        }
    }

    /**
     * Deletes a note by index after displaying current notes.
     */
    private static void deleteNote(Scanner scanner) {
        viewNotes();

        System.out.print("Enter the note number to delete (or 0 to cancel): ");
        if (scanner.hasNextInt()) {
            int num = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (num == 0) {
                System.out.println("Deletion cancelled.");
                return;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
                List<String> notes = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        notes.add(line);
                    }
                }

                if (num > 0 && num <= notes.size()) {
                    String deletedNote = notes.remove(num - 1);
                    try (FileWriter writer = new FileWriter(FILENAME)) {
                        for (String note : notes) {
                            writer.write(note + "\n");
                        }
                    }
                    System.out.println("Note deleted: " + deletedNote);
                } else {
                    System.out.println("Invalid note number.");
                }
            } catch (IOException e) {
                System.out.println("Error during deletion: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid input. Deletion cancelled.");
            scanner.nextLine(); // Clear invalid input
        }
    }
}
