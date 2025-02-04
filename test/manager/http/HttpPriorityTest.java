package manager.http;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import manager.http.token.TaskTypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import data.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpPriorityTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = taskServer.getGson();
    private static final String BASE_URL = "http://localhost:8080/priority";

    @BeforeEach
    void setUp() {
        manager.clearTasks();
        manager.clearSubTasks();
        manager.clearEpics();
        taskServer.start();
    }

    @AfterEach
    void shutDown() {
        taskServer.stop();
    }

    @Test
    void getPriority() throws IOException, InterruptedException {
        Task task1 = new Task(1, "таск1", Status.NEW, "описание1",
                LocalDateTime.of(2024, 8, 25, 20, 40), Duration.ofMinutes(15));
        Task task2 = new Task(2, "таск2", Status.NEW, "описание2",
                LocalDateTime.of(2024, 8, 24, 20, 40), Duration.ofMinutes(15));
        Task task3 = new Task(3, "таск3", Status.NEW, "описание3",
                LocalDateTime.of(2024, 8, 26, 20, 40), Duration.ofMinutes(15));

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> priorityByHttp = gson.fromJson(response.body(), new TaskTypeToken().getType());

        assertEquals(task2, priorityByHttp.get(0));
        assertEquals(task1, priorityByHttp.get(1));
        assertEquals(task3, priorityByHttp.get(2));
    }

}