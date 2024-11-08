package model;

import java.util.Objects;

public class Task {
    protected int id;
    protected TypeTask type;
    protected String name;
    protected Status status;
    protected String description;

    public Task(String name, String description) {
        this.description = description;
        this.name = name;
        this.type = TypeTask.TASK;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setType(TypeTask type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id &&
                Objects.equals(name, task.name) &&
                status == task.status &&
                Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        int hash = Objects.hash(id, name, status, description);
        hash = hash * 31;
        return hash;
    }

    @Override
    public String toString() {
        return "Task{" +
                "description='" + description + '\'' +
                ", id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}
