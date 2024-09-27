package model.business;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Adapter.DurationAdapter;
import model.Adapter.LocalTimeTypeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {
    private static InMemoryTaskManager inMemoryTaskManager;

    private Managers() {
    }

    public static TaskManager getDefault() {
        if (inMemoryTaskManager == null) {
            inMemoryTaskManager = new InMemoryTaskManager(getDefaultHistory());
        }
        return inMemoryTaskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalTimeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder.create();
    }
}
