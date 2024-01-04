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
    private static int port = 8080
    @Shared
    private static final String BASE_URL = "http://localhost:${port}/api/user"
    @Shared
    private TestRestTemplate restTemplate = new TestRestTemplate()

    @Shared
    private int id
    @Shared
    private UpdateUserRequest updateUserRequest
    @Shared
    private CreateUserRequest createUserRequest

    def setupSpec() {
        updateUserRequest = new UpdateUserRequest("UpdatedUser")
        createUserRequest = new CreateUserRequest("TestUser")

        def userResponse = restTemplate.postForEntity(BASE_URL, createUserRequest, UserResponse)
        assert userResponse.statusCode == HttpStatus.CREATED
        id = userResponse.body.id()
    }

    @Unroll
    def "should get all users"() {
        when:
        def response = restTemplate.getForEntity(BASE_URL, List)

        then:
        response.statusCode == HttpStatus.OK
        response.body != null
    }

    @Unroll
    def "should get a user by ID #userId"() {
        when:
        def response = restTemplate.getForEntity(BASE_URL + "/${userId}", UserResponse)

        then:
        response.statusCode == expectedStatusCode

        where:
        userId | expectedStatusCode
        id     | HttpStatus.OK
        0      | HttpStatus.NOT_FOUND
    }

    @Unroll
    def "should add a new user with username '#username'"() {
        given:
        def request = new CreateUserRequest(username)

        when:
        def response = restTemplate.postForEntity(BASE_URL, request, UserResponse)

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
        def response = restTemplate.exchange(BASE_URL + "/${userId}", HttpMethod.PUT, new HttpEntity<>(updateUserRequest), UserResponse)

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
        def response = restTemplate.exchange(BASE_URL + "/${id}", HttpMethod.DELETE, null, Void.class)

        then:
        response.statusCode == HttpStatus.NO_CONTENT
    }
}