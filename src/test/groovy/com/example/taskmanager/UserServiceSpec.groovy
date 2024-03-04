package com.example.taskmanager

import com.example.taskmanager.data.AppUser
import com.example.taskmanager.data.UserRepository
import com.example.taskmanager.dto.CreateUserRequest
import com.example.taskmanager.dto.UpdateUserRequest
import com.example.taskmanager.service.UserService
import spock.lang.Specification

class UserServiceSpec extends Specification {
    private UserRepository repository = Mock()

    void "should throw NullPointerException when user id is null"() {
        given:
        UserService service = new UserService(this.repository)

        when:
        service.getUserById(null)

        then:
        thrown(NullPointerException)

        and:
        0 * this.repository.findById(_)
    }

    void "should return user by id"() {
        given:
        Integer id = 1

        and:
        UserService service = new UserService(this.repository)

        when:
        Optional<AppUser> result = service.getUserById(id)

        then:
        result.isPresent()

        and:
        1 * this.repository.findById(id) >> Optional.of(Stub(AppUser))
    }

    void "should get all users"() {
        given:
        UserService service = new UserService(this.repository)
        List<AppUser> userList = Arrays.asList(Stub(AppUser), Stub(AppUser), Stub(AppUser))

        when:
        List<AppUser> result = service.getAllUsers()

        then:
        result.size() == 3

        and:
        1 * this.repository.findAll() >> userList
    }

    void "should add a new user"() {
        given:
        String username = "testuser"
        AppUser newUser = new AppUser()
        newUser.setUsername(username)
        CreateUserRequest request = new CreateUserRequest(username)

        and:
        UserService service = new UserService(this.repository)

        when:
        AppUser result = service.addUser(request)

        then:
        result.username == username

        and:
        1 * this.repository.save(newUser) >> newUser
    }

    void "should update an existing user"() {
        given:
        Integer userId = 1
        AppUser existingUser = new AppUser(username: "oldUser")
        existingUser.setId(userId)
        String updatedUsername = "updatedUser"
        UpdateUserRequest request = new UpdateUserRequest(updatedUsername)

        and:
        UserService service = new UserService(this.repository)

        when:
        Optional<AppUser> result = service.updateUser(userId, request)

        then:
        result.isPresent()

        and:
        1 * this.repository.findById(userId) >> Optional.of(existingUser)

        and:
        1 * this.repository.save(_) >> new AppUser().setUsername(updatedUsername)
    }

    void "should delete an existing user"() {
        given:
        Integer userId = 1
        AppUser existingUser = new AppUser().setUsername("userToDelete")

        and:
        UserService service = new UserService(this.repository)

        when:
        service.deleteUser(userId)

        then:
        1 * this.repository.deleteById(userId)
    }
}
