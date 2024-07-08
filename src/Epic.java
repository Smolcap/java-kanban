import java.util.ArrayList;

public class Epic extends Task{

    protected ArrayList<Integer> subTaskId = new ArrayList<>();

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public int getEpic(Subtask subtask) {
        return getId();
    }

    public void addSubtaskId(int id) {
        subTaskId.add(id);
    }

    public void cleanSubtaskId () {
        subTaskId.clear();
    }

    public ArrayList<Integer> getSubTaskId() {
        return subTaskId;
    }

    public void removeSubtaskId(int id){
        subTaskId.remove(Integer.valueOf(id));
    }


    @Override
    public String toString() {
        return "Epic{" +
                "subTaskId=" + subTaskId +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}
