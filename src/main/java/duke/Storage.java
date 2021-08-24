package duke;

import duke.Deadline;
import duke.Event;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Task> load() throws DukeException {
        try {
            File dataDirectory = new File("/data");
            dataDirectory.mkdir();
            File storage = new File("/data/duke.txt");
//            if (!Files.exists(Paths.get(filePath))) {
//                System.out.println(storage.mkdir());
//            }
            storage.createNewFile();
            Scanner savedTasks = new Scanner(storage);
            ArrayList<Task> tasks = new ArrayList<>();

            while (savedTasks.hasNextLine()) {
                String curr = savedTasks.nextLine();

                if (curr.startsWith("[T]")) {
                    Task currentTask = new ToDo(curr.substring(7));
                    tasks.add(currentTask);
                } else if (curr.startsWith("[E]")) {
                    int at = curr.lastIndexOf(" (at: ");
                    int end = curr.lastIndexOf(")");
                    Task currentTask = new Event(
                            curr.substring(7, at),
                            LocalDateTime.parse(curr.substring(at + 6, end),
                                    DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm"))
                    );
                    tasks.add(currentTask);
                } else if (curr.startsWith("[D]")) {
                    int by = curr.lastIndexOf(" (by: ");
                    int end = curr.lastIndexOf(")");
                    Task currentTask = new Deadline(
                            curr.substring(7, by),
                            LocalDateTime.parse(curr.substring(by + 6, end),
                                    DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm"))
                    );
                    tasks.add(currentTask);
                } else {
                    // do nothing
                }
            }

            return tasks;
        } catch (IOException e) {
            throw new DukeException("There is a problem loading saved data.");
        }
    }

    public void saveTasks(TaskList tasks) {
        String textToAdd = "";

        for (int i = 0; i < tasks.numOfTasks(); i++) {
            textToAdd = textToAdd + tasks.getTask(i).toString() + System.lineSeparator();
        }

        try {
            FileWriter file = new FileWriter("/data/duke.txt");
            file.write(textToAdd);
            file.close();
        } catch (IOException e) {
            System.out.println("There is a problem writing saved data.");
        }
    }
}
