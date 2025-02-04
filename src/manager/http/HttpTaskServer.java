package manager.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import manager.TaskManager;
import manager.http.adapters.DurationAdapter;
import manager.http.adapters.LocalDateTimeAdapter;
import manager.http.handlers.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    protected Gson gson;
    protected HttpServer server;
    private static final int PORT = 8080;
    public HttpTaskServer(TaskManager taskManager) {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/tasks", new TaskHandlers(taskManager, gson));
            server.createContext("/epics", new EpicHandlers(taskManager, gson));
            server.createContext("/subtasks", new SubTaskHandlers(taskManager, gson));
            server.createContext("/history", new HistoryHandler(taskManager, gson));
            server.createContext("/priority", new PriorityHandler(taskManager, gson));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка создания http-сервера на порте - " + PORT);
        }
    }
    public Gson getGson() {
        return gson;
    }
    public void start() {
        System.out.println("Сервер запушен на порту " + PORT);
        System.out.println("http://localhost:" + PORT + "/");
        server.start();
    }
    public void stop() {
        System.out.println("Сервер на порту " + PORT + " был остановлен");
        server.stop(0);
    }
}