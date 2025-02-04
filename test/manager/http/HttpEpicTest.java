package manager.http;
import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import manager.http.token.EpicTypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import data.Epic;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
class HttpEpicTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = taskServer.getGson();
    private static final String BASE_URL = "http://localhost:8080/epics";
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
    void getEpic() throws IOException, InterruptedException {
        Epic epic1 = new Epic(1, "эпик1", Status.NEW, "Описание1",
                LocalDateTime.of(2024, 7, 21, 11, 0),
                LocalDateTime.of(2024, 7, 21, 11, 15),
                Duration.ofMinutes(15));
        Epic epic2 = new Epic(2, "эпик2", Status.NEW, "Описание2",
                LocalDateTime.of(2024, 7, 22, 11, 0),
                LocalDateTime.of(2024, 7, 22, 11, 15),
                Duration.ofMinutes(15));
        Epic epic3 = new Epic(3, "эпик3", Status.NEW, "Описание3",
                LocalDateTime.of(2024, 7, 23, 11, 0),
                LocalDateTime.of(2024, 7, 23, 11, 15),
                Duration.ofMinutes(15));
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addEpic(epic3);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> fromManager = manager.showAllEpics();
        List<Epic> fromHttp = gson.fromJson(response.body(), new EpicTypeToken().getType());
        assertEquals(fromManager.size(), fromHttp.size());
        assertEquals(fromManager.get(0), fromHttp.get(0));
        assertEquals(fromManager.get(1), fromHttp.get(1));
        assertEquals(fromManager.get(2), fromHttp.get(2));
    }
    @Test
    void getEpicById() throws IOException, InterruptedException {
        Epic epic1 = new Epic(1, "эрик1", Status.NEW, "описание1",
                LocalDateTime.of(2024, 8, 24, 10, 0),
                LocalDateTime.of(2024, 8, 24, 10, 15),
                Duration.ofMinutes(15));
        manager.addEpic(epic1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/" + epic1.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic taskByHttp = gson.fromJson(response.body(), Epic.class);
        assertEquals(200, response.statusCode());
        assertEquals(epic1, taskByHttp);
    }
    @Test
    void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing epic 2");
        String taskJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Epic> tasksFromManager = manager.showAllEpics();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Testing epic 2", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }
    @Test
    void deleteEpic() throws IOException, InterruptedException {
        Epic epic1 = new Epic(1, "эрик1", Status.NEW, "описание1",
                LocalDateTime.of(2024, 8, 24, 10, 0),
                LocalDateTime.of(2024, 8, 24, 10, 15),
                Duration.ofMinutes(15));
        manager.addEpic(epic1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        assertEquals(0, manager.showAllTasks().size());
    }
    @Test
    void deleteEpicStatus404() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }
}