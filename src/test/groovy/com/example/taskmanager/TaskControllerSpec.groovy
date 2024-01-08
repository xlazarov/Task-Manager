package com.example.taskmanager

import com.example.taskmanager.data.TaskMapperImpl
import com.example.taskmanager.data.TaskRepository
import com.example.taskmanager.data.TaskState
import com.example.taskmanager.dto.CreateTaskRequest
import com.example.taskmanager.dto.UpdateTaskRequest
import com.example.taskmanager.service.TaskService
import com.example.taskmanager.web.TaskController
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.JsonSlurper
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Unroll

import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

@Stepwise
class TaskControllerSpec extends Specification {

    def taskRepository = Mock(TaskRepository)
    def taskService = new TaskService(taskRepository)
    def taskMapper = new TaskMapperImpl()
    def taskController = new TaskController(taskService, taskMapper)

    MockMvc mockMvc

    private static int id
    private UpdateTaskRequest updateTaskRequest
    private CreateTaskRequest createTaskRequest

    def objectMapper = new ObjectMapper()

    void setup() {
        mockMvc = standaloneSetup(taskController).build()

        updateTaskRequest = new UpdateTaskRequest("UpdatedTask", LocalDate.now().plusDays(7), null, TaskState.IN_PROGRESS)
        createTaskRequest = new CreateTaskRequest("TestTask", null, null, TaskState.TODO)
//        taskService.createTask(_) >> new TaskResponse(1, "TestTask", null, null, TaskState.TODO)
//        taskService.getAllTasks() >> [new TaskResponse(1, "TestTask", null, null, TaskState.TODO)]
//        taskService.getTaskById(1) >> new TaskResponse(1, "TestTask", null, null, TaskState.TODO)
//        taskService.getTaskById(0) >> null
//        def taskResponse = mockMvc.perform(post("/api/task")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(createTaskRequest)))
//                .andExpect(status().isCreated())
//                .andReturn().response
//
//        println("Response Content: ${taskResponse.contentAsString}")
//        def content = new JsonSlurper().parseText(taskResponse.contentAsString)
//        id = content.id
    }

    @Unroll
    def "should get all tasks"() {
        when:
        def response = mockMvc.perform(get("/api/task"))

        then:
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Unroll
    def "should get a task by ID #taskId"() {
        when:
        def response = mockMvc.perform(get("/api/task/$taskId"))

        then:
        response.andExpect(expectedStatusCode)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        where:
        taskId | expectedStatusCode
        id     | status().isOk()
        0      | status().isNotFound()
    }

    @Unroll
    def "should add a new task with description '#description'"() {
        given:
        def request = new CreateTaskRequest(description, null, null, TaskState.TODO)

        when:
        def response = mockMvc.perform(post("/api/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

        then:
        response.andExpect(expectedStatusCode)
                .andExpect(expectedContent)

        where:
        description | expectedStatusCode      | expectedContent
        "AddTask"   | status().isCreated()    | content().contentType(MediaType.APPLICATION_JSON)
        null        | status().isBadRequest() | content().string("")
        ""          | status().isBadRequest() | content().string("")
        "  "        | status().isBadRequest() | content().string("")
    }

    @Unroll
    def "should update or return not found for task with ID #taskId"() {
        when:
        def response = mockMvc.perform(put("/api/task/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

        then:
        response.andExpect(expectedStatusCode)

        where:
        taskId | expectedStatusCode
        id     | status().isNoContent()
        0      | status().isNotFound()
    }

    @Unroll
    def "should update the state of an existing task to '#state'"() {
        given:
        def request = new UpdateTaskRequest(null, null, null, state)

        when:
        def response = mockMvc.perform(put("/api/task/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

        then:
        response.andExpect(expectedStatusCode)

        where:
        state                 | expectedStatusCode
        TaskState.TODO        | status().isNoContent()
        TaskState.IN_PROGRESS | status().isNoContent()
        TaskState.COMPLETED   | status().isNoContent()
        TaskState.DELAYED     | status().isNoContent()
        null                  | status().isNoContent()
    }

    @Unroll
    def "should update the date of an existing task to '#dueDate' or return bad request"() {
        given:
        def request = new UpdateTaskRequest(null, dueDate, null, null)

        when:
        def response = mockMvc.perform(put("/api/task/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        then:
        response.andExpect(expectedStatusCode)

        where:
        dueDate                      | expectedStatusCode
        LocalDate.now()              | status().isBadRequest()
        LocalDate.now().plusDays(7)  | status().isNoContent()
        LocalDate.now().minusDays(7) | status().isBadRequest()
        null                         | status().isNoContent()
    }

    @Unroll
    def "should delete an existing task"() {
        when:
        def response = mockMvc.perform(delete("/api/task/$id"))

        then:
        response.andExpect(status().isNoContent())
    }

    @Unroll
    def "should get tasks by state '#state'"() {
        when:
        def response = mockMvc.perform(get("/api/task/state/$state"))

        then:
        response.andExpect(expectedStatusCode)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        where:
        state                 | expectedStatusCode
        TaskState.TODO        | status().isOk()
        TaskState.IN_PROGRESS | status().isOk()
        TaskState.COMPLETED   | status().isOk()
        TaskState.DELAYED     | status().isOk()
    }

    @Unroll
    def "should get tasks for user with ID #userId"() {
        when:
        def response = mockMvc.perform(get("/api/task/user/$userId"))

        then:
        response.andExpect(expectedStatusCode)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        where:
        userId | expectedStatusCode
        id     | status().isOk()
        0      | status().isOk()
    }

    @Unroll
    def "should get tasks by due date '#dueDate'"() {
        when:
        def response = mockMvc.perform(get("/api/task/date/$dueDate"))

        then:
        response.andExpect(expectedStatusCode)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        where:
        dueDate                      | expectedStatusCode
        LocalDate.now().plusDays(7)  | status().isOk()
        LocalDate.now()              | status().isOk()
        LocalDate.now().minusDays(7) | status().isOk()
    }
}