package model.business;

import model.Task;
import org.junit.platform.engine.support.hierarchical.Node;

import java.util.List;

public interface HistoryManager {

    List<Task> getHistory();

    void add(Task task);

    void remove(int id);

    void linkLast(Task task);

    List<Task> getTasks();
}
