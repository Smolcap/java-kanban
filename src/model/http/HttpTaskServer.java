package model.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import model.business.Managers;
import model.business.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer;
    private final Gson gson;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.gson = Managers.getGson();
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
    }

    public void start() {
        httpServer.createContext("/tasks", new TaskHandler(taskManager,gson));
        httpServer.createContext("/epics", new EpicHandler(taskManager, gson));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager,gson));
        httpServer.createContext("/history", new HistoryHandler(Managers.getDefaultHistory(), gson));
        httpServer.createContext("/prioritized",new PrioritizedHandler(taskManager, gson));

        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(1);
    }

    public static void main(String[] args) throws IOException {
        try {
            TaskManager taskManager = Managers.getDefault();
            HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
            httpTaskServer.start();
        } catch (IOException e) {
            System.err.println("Ошибка при запуске HTTP-сервера: " + e.getMessage());
        }
    }
}

