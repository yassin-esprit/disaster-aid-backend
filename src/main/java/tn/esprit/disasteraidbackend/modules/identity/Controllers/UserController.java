package tn.esprit.disasteraidbackend.modules.identity.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.retrieveAllUsers());
    }

    @Operation(summary = "Récupérer un utilisateur par ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "ID de l'utilisateur")
            @PathVariable Integer id) {
        return ResponseEntity.ok(userService.retrieveUser(id));
    }

    @Operation(summary = "Ajouter un nouvel utilisateur")
    @PostMapping
    public ResponseEntity<UserResponseDTO> addUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userRequestDTO));
    }

    @Operation(summary = "Mettre à jour un utilisateur")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "ID de l'utilisateur à modifier")
            @PathVariable Integer id,
            @Valid @RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userRequestDTO));
    }

    @Operation(summary = "Supprimer un utilisateur par ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeUser(
            @Parameter(description = "ID de l'utilisateur à supprimer")
            @PathVariable Integer id) {
        userService.removeUser(id);
        return ResponseEntity.noContent().build();
    }
}