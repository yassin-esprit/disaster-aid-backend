package tn.esprit.disasteraidbackend.User.IdentityAccess.Controllers;

import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.User.IdentityAccess.Entities.User;
import tn.esprit.disasteraidbackend.User.IdentityAccess.Services.UserService;

import java.util.List;

@RestController
@RequestMapping("/identity-access/users")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    // ✅ constructeur manuel (IMPORTANT)
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/retrieve-all-users")
    public List<User> getAllUsers() {
        return userService.retrieveAllUsers();
    }

    @GetMapping("/retrieve-user/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.retrieveUser(id);
    }

    @PostMapping("/add-user")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("/update-user")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/remove-user/{id}")
    public void removeUser(@PathVariable Integer id) {
        userService.removeUser(id);
    }
}