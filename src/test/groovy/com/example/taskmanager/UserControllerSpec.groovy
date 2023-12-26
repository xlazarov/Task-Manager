package com.example.taskmanager

import com.example.taskmanager.dto.CreateUserRequest
import com.example.taskmanager.dto.UserResponse
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus

import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerSpec extends Specification{
    @LocalServerPort
    private int port = 8080

    private TestRestTemplate restTemplate = new TestRestTemplate()

    def "should add a new user"() {
        given:
        def createUserRequest = new CreateUserRequest("TestUser")

        when:
        def response = restTemplate.postForEntity("http://localhost:${port}/api/user/", createUserRequest, UserResponse)

        then:
        response.statusCode == HttpStatus.CREATED
        response.body != null
    }



}
