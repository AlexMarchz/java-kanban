package manager.http;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import manager.http.token.SubTaskTypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.Status;
import data.Epic;
import data.SubTask;

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

class HttpSubTaskTest {

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = taskServer.getGson();
    private static final String BASE_URL = "http://localhost:8080/subtasks";

    @BeforeEach
    void setUp() {
        manager.clearTasks();
        manager.clearSubTasks();
        manager.clearEpics();
        taskServer.start();
        Epic epic = new Epic("Test 2", "Testing epic 2");
        manager.addEpic(epic);
    }

    @AfterEach
    void shutDown() {
        taskServer.stop();
    }

    @Test
    void getSubtask() throws IOException, InterruptedException {
        SubTask subtask1 = new SubTask(2, "subtask1", Status.NEW, "description1",
                1, LocalDateTime.of(2024, 8, 24, 20, 40), Duration.ofMinutes(15));
        SubTask subtask2 = new SubTask(3, "subtask2", Status.NEW, "description2",
                1, LocalDateTime.of(2024, 8, 25, 20, 40), Duration.ofMinutes(15));
        SubTask subtask3 = new SubTask(4, "subtask3", Status.NEW, "description3",
                1, LocalDateTime.of(2024, 8, 26, 20, 40), Duration.ofMinutes(15));

        manager.addSubTask(subtask1);
        manager.addSubTask(subtask2);
        manager.addSubTask(subtask3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<SubTask> fromManager = manager.showAllSubTasks();
        List<SubTask> fromHttp = gson.fromJson(response.body(), new SubTaskTypeToken().getType());

        assertEquals(fromManager.size(), fromHttp.size());
        assertEquals(fromManager.get(0), fromHttp.get(0));
        assertEquals(fromManager.get(1), fromHttp.get(1));
        assertEquals(fromManager.get(2), fromHttp.get(2));
    }

    @Test
    void getSubtaskById() throws IOException, InterruptedException {
        SubTask subtask3 = new SubTask(4, "subtask3", Status.NEW, "description3",
                1, LocalDateTime.of(2024, 8, 26, 20, 40), Duration.ofMinutes(15));
        manager.addSubTask(subtask3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/" + subtask3.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subtaskByHttp = gson.fromJson(response.body(), SubTask.class);

        assertEquals(200, response.statusCode());
        assertEquals(subtask3, subtaskByHttp);
    }

    @Test
    void testAddSubtask() throws IOException, InterruptedException {
        SubTask subtask = new SubTask("Test 2", "Testing subtask 2", 1);
        subtask.setDuration(Duration.ofMinutes(5));
        subtask.setStartTime(LocalDateTime.now());

        String taskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<SubTask> tasksFromManager = manager.showAllSubTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Testing subtask 2", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    void testUpdateSubtask() throws IOException, InterruptedException {
        SubTask subtask1 = new SubTask(2, "сабтаск1", Status.NEW, "описание1",
                1, LocalDateTime.now().minusHours(2), Duration.ofMinutes(15));
        SubTask newSubtask1 = new SubTask(2, "сабтаск2", Status.NEW, "описание2",
                1, LocalDateTime.now().minusHours(1), Duration.ofMinutes(15));
        manager.addSubTask(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/" + subtask1.getId());
        String subtaskJson = gson.toJson(newSubtask1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(newSubtask1, manager.findSubTaskById(2), "Задача не совпадает.");
    }

    @Test
    void testStatusCode406() throws IOException, InterruptedException {
        SubTask subtask1 = new SubTask(2, "сабтаск1", Status.NEW, "описание1",
                1, LocalDateTime.now(), Duration.ofMinutes(15));
        SubTask newSubtasks2 = new SubTask(3, "сабтаск2", Status.IN_PROGRESS, "описание2",
                1, LocalDateTime.now(), Duration.ofMinutes(15));
        manager.addSubTask(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/" + subtask1.getId());
        String taskJson = gson.toJson(newSubtasks2);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());
    }

    @Test
    void deleteSubtask() throws IOException, InterruptedException {
        SubTask subtask1 = new SubTask("сабтаск1", "описание1", 1);
        manager.addSubTask(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/" + subtask1.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(204, response.statusCode());
        assertEquals(0, manager.showAllSubTasks().size());
    }

    @Test
    void deleteTaskStatus404() throws IOException, InterruptedException {
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
