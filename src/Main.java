import model.business.TaskManager;
import model.Epic;
import model.Task;
import model.Subtask;
import model.Status;

import java.util.ArrayList;


public class Main {


    public static void main(String[] args) {

        TaskManager manager = new TaskManager();
        Task task1 = new Task("Task1", "Description task1");
        final int taskId1 = manager.addNewTask(task1);
        Task task3 = manager.getTaskId(taskId1);
        ArrayList<Task> tasks = manager.getTasks();
        assert(task3.getId() == task1.getId());
        System.out.println("all good");




        Task task2 = new Task("Task2", "Description task2");
        final int taskId2 = manager.addNewTask(task2);

        Epic epic1 = new Epic("Epic1", "Description epic1");
        final int epicId1 = manager.addNewEpic(epic1);
        Epic epic4 = manager.getEpicId(epicId1);
        ArrayList<Epic> epics = manager.getEpics();
        assert (epic1.getId() == epic4.getId());
        System.out.println("good");



        Epic epic2 = new Epic("Epic2", "Description epic2");



    }
}
