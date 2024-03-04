package com.example.taskmanager

import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskRepository
import com.example.taskmanager.data.TaskState
import com.example.taskmanager.dto.CreateTaskRequest
import com.example.taskmanager.dto.UpdateTaskRequest
import com.example.taskmanager.service.TaskService
import spock.lang.Specification

import java.time.LocalDate

class TaskServiceSpec extends Specification {
    private TaskRepository repository = Mock()

    void "should throw NullPointerException when task id is null"() {
        given:
        TaskService service = new TaskService(this.repository)

        when:
        service.getTaskById(null)

        then:
        thrown(NullPointerException)

        and:
        0 * this.repository.findById(null)
    }

    void "should return task by id"() {
        given:
        Integer id = 1

        and:
        TaskService service = new TaskService(this.repository)

        when:
        Optional<Task> result = service.getTaskById(id)

        then:
        result.isPresent()

        and:
        1 * this.repository.findById(id) >> Optional.of(Stub(Task))
    }

    void "should get all tasks"() {
        given:
        TaskService service = new TaskService(this.repository)
        List<Task> taskList = Arrays.asList(Stub(Task), Stub(Task), Stub(Task))

        when:
        List<Task> result = service.getAllTasks()

        then:
        result.size() == 3

        and:
        1 * this.repository.findAll() >> taskList
    }

    void "should get tasks by state"() {
        given:
        TaskState state = TaskState.DELAYED
        List<Task> taskList = Arrays.asList(Stub(Task).setState(state), Stub(Task), Stub(Task))

        and:
        TaskService service = new TaskService(this.repository)

        when:
        List<Task> result = service.getTasksByState(state)

        then:
        result.size() == 1

        then:
        1 * this.repository.findByState(state) >> taskList.subList(0, 1)
    }

    void "should get tasks by due date"() {
        given:
        LocalDate dueDate = LocalDate.now() + 1
        List<Task> taskList = Arrays.asList(Stub(Task).setDueDate(dueDate), Stub(Task), Stub(Task))

        and:
        TaskService service = new TaskService(this.repository)

        when:
        List<Task> result = service.getTasksByDueDate(dueDate)

        then:
        result.size() == 1

        then:
        1 * this.repository.findByDueDate(dueDate) >> taskList.subList(0, 1)
    }

    void "should add a new task"() {
        given:
        String description = "testtask"
        Task newTask = new Task(description: description, state: TaskState.TODO)
        CreateTaskRequest request = new CreateTaskRequest(description, null, null, TaskState.TODO)

        and:
        TaskService service = new TaskService(this.repository)

        when:
        Task result = service.addTask(request)

        then:
        result.description == description

        and:
        1 * this.repository.save(newTask) >> newTask
    }

    void "should update an existing task"() {
        given:
        Integer taskId = 1
        Task existingTask = new Task(description: "oldDescription", state: TaskState.TODO)
        existingTask.setId(taskId)
        String newDescription = "newDescription"
        UpdateTaskRequest request = new UpdateTaskRequest(newDescription, null, null, TaskState.TODO)

        and:
        TaskService service = new TaskService(this.repository)

        when:
        Optional<Task> result = service.updateTask(taskId, request)

        then:
        result.isPresent()

        and:
        1 * this.repository.findById(taskId) >> Optional.of(existingTask)

        and:
        1 * this.repository.save(_) >> new Task().setDescription(newDescription)
    }

    void "should delete an existing task"() {
        given:
        Integer taskId = 1
        Task existingTask = new Task().setDescription("TaskToDelete")

        and:
        TaskService service = new TaskService(this.repository)

        when:
        service.deleteTask(taskId)

        then:
        1 * this.repository.deleteById(taskId)
    }
}
