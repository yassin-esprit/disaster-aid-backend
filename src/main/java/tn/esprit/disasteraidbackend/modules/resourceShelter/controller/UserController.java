package tn.esprit.disasteraidbackend.modules.resourceShelter.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.resourceShelter.dto.UserCreateDto;
import tn.esprit.disasteraidbackend.modules.resourceShelter.dto.UserUpdateDto;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.User;
import tn.esprit.disasteraidbackend.modules.resourceShelter.service.IUserService;

@RestController
@RequestMapping("/resource-shelter/users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User add(@Valid @RequestBody UserCreateDto dto) {
        User u = new User();
        u.setName(dto.getName());
        return userService.addUser(u);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Integer id, @Valid @RequestBody UserUpdateDto dto) {
        User u = userService.getUser(id);
        if (u == null) return null;
        u.setName(dto.getName());
        return userService.updateUser(u);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}