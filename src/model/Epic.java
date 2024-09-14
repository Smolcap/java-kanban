package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    protected List<Integer> subtaskId = new ArrayList<>();
    protected LocalDateTime endTime;


    public Epic(String name, String description) {
        super(name, description);
        this.type = TypeTask.EPIC;
        this.status = Status.NEW;
        this.endTime = startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getEpic(Subtask subtask) {
        return getId();
    }

    public void addSubtaskId(int id) {
        subtaskId.add(id);
    }

    public void cleanSubtaskId() {
        subtaskId.clear();
    }

    public List<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void removeSubtaskId(int id) {
        subtaskId.remove(Integer.valueOf(id));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskId, epic.subtaskId) &&
                Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskId=" + subtaskId +
                ", endTime=" + endTime +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}
