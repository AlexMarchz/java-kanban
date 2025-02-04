package manager.http;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import manager.http.token.TaskTypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import data.Epic;
import data.SubTask;
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

class HttpHistoryTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = taskServer.getGson();
    private static final String BASE_URL = "http://localhost:8080/history";

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
    void getHistory() throws IOException, InterruptedException {
        Task task1 = new Task(1, "таска", Status.NEW, "описание",
                LocalDateTime.of(2024, 8, 24, 20, 40), Duration.ofMinutes(15));
        Epic epic2 = new Epic(1, "эпик", Status.NEW, "описание",
                LocalDateTime.of(2024, 8, 24, 10, 0),
                LocalDateTime.of(2024, 8, 24, 10, 15),
                Duration.ofMinutes(15));
        SubTask subtask3 = new SubTask(4, "сабтаск", Status.NEW, "описание",
                2, LocalDateTime.of(2024, 8, 26, 20, 40), Duration.ofMinutes(15));

        manager.addTask(task1);
        manager.addEpic(epic2);
        manager.addSubTask(subtask3);

        manager.findEpicById(epic2.getId());
        manager.findTaskById(task1.getId());
        manager.findSubTaskById(subtask3.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> historyByHttp = gson.fromJson(response.body(), new TaskTypeToken().getType());

        assertEquals(200, response.statusCode());
        assertEquals(3, historyByHttp.size());
        assertEquals(epic2.getId(), historyByHttp.get(0).getId());
        assertEquals(task1.getId(), historyByHttp.get(1).getId());
        assertEquals(subtask3.getId(), historyByHttp.get(2).getId());
    }
}
