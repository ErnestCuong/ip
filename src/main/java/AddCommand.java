import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddCommand implements Command {
    private String userInput;

    public AddCommand(String userInput) {
        super();
        this.userInput = userInput;
    }

    public static void addToDo(String s, TaskList tasks, Ui ui) throws DukeException {
        try {
            Task curr = new ToDo(s.substring(5));
            tasks.addTask(curr);
            ui.showAddTask(curr, tasks.numOfTasks());

        } catch (IndexOutOfBoundsException e) {
            throw new DukeException("The description of a todo cannot be empty.");
        }
    }

    public static void addEvent(String s, TaskList tasks, Ui ui) throws DukeException {
        try {
            int at = s.lastIndexOf(" /at ");
            Task curr = new Event(
                    s.substring(6, at),
                    LocalDateTime.parse(s.substring(at + 5), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            );
            tasks.addTask(curr);
            ui.showAddTask(curr, tasks.numOfTasks());
        } catch (IndexOutOfBoundsException e) {
            throw new DukeException("The description and time of an event cannot be empty.");
        } catch (DateTimeParseException e) {
            throw new DukeException("The time must be in this format: yyyy-MM-dd HH:mm");
        }
    }

    public static void addDeadline(String s, TaskList tasks, Ui ui) throws DukeException {
        try {
            int by = s.lastIndexOf(" /by ");
            Task curr = new Deadline(
                    s.substring(9, by),
                    LocalDateTime.parse(s.substring(by + 5), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            );
            tasks.addTask(curr);
            ui.showAddTask(curr, tasks.numOfTasks());
        } catch (IndexOutOfBoundsException e) {
            throw new DukeException("The description and time of a deadline cannot be empty.");
        } catch (DateTimeParseException e) {
            throw new DukeException("The time must be in this format: yyyy-MM-dd HH:mm");
        }
    }



    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeException {
        if (userInput.startsWith("todo")) {
            addToDo(userInput, tasks, ui);
            storage.saveTasks(tasks);
        } else if (userInput.startsWith("event")) {
            addEvent(userInput, tasks, ui);
            storage.saveTasks(tasks);
        } else if (userInput.startsWith("deadline")) {
            addDeadline(userInput, tasks, ui);
            storage.saveTasks(tasks);
        } else { // any other input from user
            throw new DukeException("I'm sorry, but I don't know what that means :-(");
        }
    };

    @Override
    public boolean isExit() {
        return false;
    }
}
