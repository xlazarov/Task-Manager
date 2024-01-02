package com.example.taskmanager

import com.example.taskmanager.dto.CreateUserRequest
import com.example.taskmanager.dto.UpdateUserRequest
import com.example.taskmanager.dto.UserResponse
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerSpec extends Specification {
    @Shared
    @LocalServerPort
    private int port = 8080
    @Shared
    private TestRestTemplate restTemplate = new TestRestTemplate()

    @Shared
    int id
    @Shared
    UpdateUserRequest updateUserRequest
    @Shared
    CreateUserRequest createUserRequest

    def setupSpec() {
        updateUserRequest = new UpdateUserRequest("UpdatedUser")
        createUserRequest = new CreateUserRequest("TestUser")

        def userResponse = restTemplate.postForEntity("http://localhost:${port}/api/user", createUserRequest, UserResponse)
        assert userResponse.statusCode == HttpStatus.CREATED
        id = userResponse.body.id()
    }

    @Unroll
    def "should add a new user with username '#username'"() {
        given:
        def request = new CreateUserRequest(username)

        when:
        def response = restTemplate.postForEntity("http://localhost:${port}/api/user", request, UserResponse)

        then:
        response.statusCode == expectedStatusCode
        response.body != null

        where:
        username  | expectedStatusCode
        "AddUser" | HttpStatus.CREATED
        null      | HttpStatus.BAD_REQUEST
        ""        | HttpStatus.BAD_REQUEST
        "  "      | HttpStatus.BAD_REQUEST
    }

    @Unroll
    def "should update or return not found for user with ID #userId"() {
        when:
        def response = restTemplate.exchange("http://localhost:${port}/api/user/${userId}", HttpMethod.PUT, new HttpEntity<>(updateUserRequest), UserResponse)

        then:
        response.statusCode == expectedStatusCode

        where:
        userId | expectedStatusCode
        id     | HttpStatus.NO_CONTENT
        0      | HttpStatus.NOT_FOUND
    }

    @Unroll
    def "should delete an existing user"() {
        when:
        def response = restTemplate.exchange("http://localhost:${port}/api/user/${id}", HttpMethod.DELETE, null, Void.class)

        then:
        response.statusCode == HttpStatus.NO_CONTENT
    }
}