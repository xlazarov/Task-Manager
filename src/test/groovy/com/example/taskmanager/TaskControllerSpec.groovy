package com.example.taskmanager

import com.example.taskmanager.data.TaskState
import com.example.taskmanager.dto.CreateTaskRequest
import com.example.taskmanager.dto.TaskResponse
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import spock.lang.Specification


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerSpec extends Specification {
    @LocalServerPort
    private int port = 8080

    private TestRestTemplate restTemplate = new TestRestTemplate()

    def "should add a new task"() {
        given:
        def createTaskRequest = new CreateTaskRequest("TestTask", null, null, TaskState.TODO.name())

        when:
        def response = restTemplate.postForEntity("http://localhost:${port}/api/task", createTaskRequest, TaskResponse)

        then:
        response.statusCode == HttpStatus.CREATED
        response.body != null
    }

}
