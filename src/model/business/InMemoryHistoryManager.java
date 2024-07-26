package model.business;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private  final ArrayList<Task> viewingHistory = new ArrayList<>();
    private final static int LIMIT = 10;

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(viewingHistory);
    }

    @Override
    public void add(Task task) {
        if(task == null) {
            return;
        }
        viewingHistory.add(task);
        if(viewingHistory.size() > LIMIT) {
            viewingHistory.remove(0);
        }

    }
}
