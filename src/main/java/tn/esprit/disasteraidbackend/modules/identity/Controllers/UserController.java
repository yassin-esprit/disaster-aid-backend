package tn.esprit.disasteraidbackend.modules.identity.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.identity.DTOs.UserRequestDTO;
import tn.esprit.disasteraidbackend.modules.identity.DTOs.UserResponseDTO;
import tn.esprit.disasteraidbackend.modules.identity.Services.UserService;

import java.util.List;

@RestController
@RequestMapping("/identity-access/users")
@CrossOrigin("*")
@Tag(name = "User", description = "Gestion des utilisateurs")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Récupérer tous les utilisateurs")
    @GetMapping("/retrieve-all-users")
    public List<UserResponseDTO> getAllUsers() {
        return userService.retrieveAllUsers();
    }

    @Operation(summary = "Récupérer un utilisateur par ID")
    @GetMapping("/retrieve-user/{id}")
    public UserResponseDTO getUserById(
            @Parameter(description = "ID de l'utilisateur")
            @PathVariable Integer id) {
        return userService.retrieveUser(id);
    }

    @Operation(summary = "Ajouter un nouvel utilisateur")
    @PostMapping("/add-user")
    public UserResponseDTO addUser(@RequestBody UserRequestDTO userRequestDTO) {
        return userService.addUser(userRequestDTO);
    }

    @Operation(summary = "Mettre à jour un utilisateur")
    @PutMapping("/update-user/{id}")
    public UserResponseDTO updateUser(
            @Parameter(description = "ID de l'utilisateur à modifier")
            @PathVariable Integer id,
            @RequestBody UserRequestDTO userRequestDTO) {
        return userService.updateUser(id, userRequestDTO);
    }

    @Operation(summary = "Supprimer un utilisateur par ID")
    @DeleteMapping("/remove-user/{id}")
    public void removeUser(
            @Parameter(description = "ID de l'utilisateur à supprimer")
            @PathVariable Integer id) {
        userService.removeUser(id);
    }
}