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
class TaskControllerSpec extends Specification {
    @Shared
    @LocalServerPort
    private int port = 8080
    @Shared
    private TestRestTemplate restTemplate = new TestRestTemplate()

    @Shared
    int id
    @Shared
    UpdateTaskRequest updateTaskRequest
    @Shared
    CreateTaskRequest createTaskRequest

    def setupSpec() {
        updateTaskRequest = new UpdateTaskRequest("UpdatedTask", LocalDate.now().plusDays(7), null, TaskState.IN_PROGRESS)
        createTaskRequest = new CreateTaskRequest("TestTask", null, null, TaskState.TODO)

        def taskResponse = restTemplate.postForEntity("http://localhost:${port}/api/task", createTaskRequest, TaskResponse)
        assert taskResponse.statusCode == HttpStatus.CREATED
        id = taskResponse.body.id()
    }

    @Unroll
    def "should add a new task with description '#description'"() {
        given:
        def request = new CreateTaskRequest(description, null, null, TaskState.TODO)

        when:
        def response = restTemplate.postForEntity("http://localhost:${port}/api/task", request, TaskResponse)

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
        def response = restTemplate.exchange("http://localhost:${port}/api/task/${taskId}", HttpMethod.PUT, new HttpEntity<>(updateTaskRequest), TaskResponse)

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
        def response = restTemplate.exchange("http://localhost:${port}/api/task/${id}", HttpMethod.PUT, new HttpEntity<>(request), Object)

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
        def response = restTemplate.exchange("http://localhost:${port}/api/task/${id}", HttpMethod.PUT, new HttpEntity<>(request), Object)

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
        def response = restTemplate.exchange("http://localhost:${port}/api/task/${id}", HttpMethod.DELETE, null, Void.class)

        then:
        response.statusCode == HttpStatus.NO_CONTENT
    }


}


