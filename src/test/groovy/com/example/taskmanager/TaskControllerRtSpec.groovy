package com.example.taskmanager

import com.example.taskmanager.data.TaskState
import com.example.taskmanager.dto.CreateTaskRequest
import com.example.taskmanager.dto.TaskResponse
import com.example.taskmanager.dto.UpdateTaskRequest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Unroll

import java.time.LocalDate


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Stepwise
class TaskControllerRtSpec extends Specification {
    @Shared
    @LocalServerPort
    private static int port = 8080
    @Shared
    private static final String BASE_URL = "http://localhost:${port}/api/task"
    @Shared
    private TestRestTemplate restTemplate = new TestRestTemplate()

    @Shared
    private int id
    @Shared
    private UpdateTaskRequest updateTaskRequest
    @Shared
    private CreateTaskRequest createTaskRequest

    def setupSpec() {
        updateTaskRequest = new UpdateTaskRequest("UpdatedTask", LocalDate.now().plusDays(7), null, TaskState.IN_PROGRESS)
        createTaskRequest = new CreateTaskRequest("TestTask", null, null, TaskState.TODO)

        def taskResponse = restTemplate.postForEntity(BASE_URL, createTaskRequest, TaskResponse)
        assert taskResponse.statusCode == HttpStatus.CREATED
        id = taskResponse.body.id()
    }

    @Unroll
    def "should get all tasks"() {
        when:
        def response = restTemplate.getForEntity(BASE_URL, List)

        then:
        response.statusCode == HttpStatus.OK
        response.body != null
    }

    @Unroll
    def "should get a task by ID #taskId"() {
        when:
        def response = restTemplate.getForEntity(BASE_URL + "/${taskId}", TaskResponse)

        then:
        response.statusCode == expectedStatusCode

        where:
        taskId | expectedStatusCode
        id     | HttpStatus.OK
        0      | HttpStatus.NOT_FOUND
    }

    @Unroll
    def "should add a new task with description '#description'"() {
        given:
        def request = new CreateTaskRequest(description, null, null, TaskState.TODO)

        when:
        def response = restTemplate.postForEntity(BASE_URL, request, TaskResponse)

        then:
        response.statusCode == expectedStatusCode
        response.body != null

        where:
        description | expectedStatusCode
        "AddTask"   | HttpStatus.CREATED
        null        | HttpStatus.BAD_REQUEST
        ""          | HttpStatus.BAD_REQUEST
        "  "        | HttpStatus.BAD_REQUEST
    }

    @Unroll
    def "should update or return not found for task with ID #taskId"() {
        when:
        def response = restTemplate.exchange(BASE_URL + "/${taskId}", HttpMethod.PUT, new HttpEntity<>(updateTaskRequest), TaskResponse)

        then:
        response.statusCode == expectedStatusCode

        where:
        taskId | expectedStatusCode
        id     | HttpStatus.NO_CONTENT
        0      | HttpStatus.NOT_FOUND
    }

    @Unroll
    def "should update the state of an existing task to '#state' or return bad request"() {
        given:
        def request = new UpdateTaskRequest(null, null, null, state)

        when:
        def response = restTemplate.exchange(BASE_URL + "/${id}", HttpMethod.PUT, new HttpEntity<>(request), Object)

        then:
        response.statusCode == expectedStatusCode

        where:
        state                 | expectedStatusCode
        TaskState.TODO        | HttpStatus.NO_CONTENT
        TaskState.IN_PROGRESS | HttpStatus.NO_CONTENT
        TaskState.COMPLETED   | HttpStatus.NO_CONTENT
        TaskState.DELAYED     | HttpStatus.NO_CONTENT
        null                  | HttpStatus.NO_CONTENT
    }

    @Unroll
    def "should update the date of an existing task to '#dueDate' or return bad request"() {
        given:
        def request = new UpdateTaskRequest(null, dueDate, null, null)

        when:
        def response = restTemplate.exchange(BASE_URL + "/${id}", HttpMethod.PUT, new HttpEntity<>(request), Object)

        then:
        response.statusCode == expectedStatusCode

        where:
        dueDate                      | expectedStatusCode
        LocalDate.now()              | HttpStatus.BAD_REQUEST
        LocalDate.now().plusDays(7)  | HttpStatus.NO_CONTENT
        LocalDate.now().minusDays(7) | HttpStatus.BAD_REQUEST
        null                         | HttpStatus.NO_CONTENT
    }

    @Unroll
    def "should delete an existing task"() {
        when:
        def response = restTemplate.exchange(BASE_URL + "/${id}", HttpMethod.DELETE, null, Void.class)

        then:
        response.statusCode == HttpStatus.NO_CONTENT
    }

    @Unroll
    def "should get tasks by state '#state'"() {
        when:
        def response = restTemplate.getForEntity(BASE_URL + "/state/${state}", List)

        then:
        response.statusCode == expectedStatusCode
        response.body != null

        where:
        state                 | expectedStatusCode
        TaskState.TODO        | HttpStatus.OK
        TaskState.IN_PROGRESS | HttpStatus.OK
        TaskState.COMPLETED   | HttpStatus.OK
        TaskState.DELAYED     | HttpStatus.OK
    }

    @Unroll
    def "should get tasks for user with ID #userId"() {
        when:
        def response = restTemplate.getForEntity(BASE_URL + "/user/${userId}", List)

        then:
        response.statusCode == expectedStatusCode
        response.body != null

        where:
        userId | expectedStatusCode
        id     | HttpStatus.OK
        0      | HttpStatus.OK
    }

    @Unroll
    def "should get tasks by due date '#dueDate'"() {
        when:
        def response = restTemplate.getForEntity(BASE_URL + "/date/${dueDate}", List)

        then:
        response.statusCode == HttpStatus.OK
        response.body != null

        and:
        // Checks if "state" is set to "DELAYED" if "dueDate" is in the past
        response.body.every { task ->
            dueDate.isBefore(LocalDate.now()) ? task.state == TaskState.DELAYED : true
        }

        where:
        dueDate                      | expectedStatusCode
        LocalDate.now().plusDays(7)  | HttpStatus.OK
        LocalDate.now()              | HttpStatus.OK
        LocalDate.now().minusDays(7) | HttpStatus.OK
    }
}
