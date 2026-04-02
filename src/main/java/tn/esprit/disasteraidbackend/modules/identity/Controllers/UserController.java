package tn.esprit.disasteraidbackend.modules.identity.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.identity.DTOs.UserRequestDTO;
import tn.esprit.disasteraidbackend.modules.identity.DTOs.UserResponseDTO;
import tn.esprit.disasteraidbackend.modules.identity.Services.UserService;

import java.util.List;

@RestController
@RequestMapping("/identity-access/users")
@CrossOrigin("*")
@Tag(name = "User", description = "User management")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Retrieve all users")
    @PreAuthorize("hasAnyRole('PLATFORM_ADMIN','ORG_ADMIN','AGENCY_ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.retrieveAllUsers());
    }

    @Operation(summary = "Retrieve a user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "User ID")
            @PathVariable Integer id) {
        return ResponseEntity.ok(userService.retrieveUser(id));
    }

    @Operation(summary = "Create a new user")
    @PreAuthorize("hasAnyRole('PLATFORM_ADMIN','ORG_ADMIN','AGENCY_ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponseDTO> addUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userRequestDTO));
    }

    @Operation(summary = "Update a user")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "User ID to update")
            @PathVariable Integer id,
            @Valid @RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userRequestDTO));
    }

    @Operation(summary = "Delete a user by ID")
    @PreAuthorize("hasAnyRole('PLATFORM_ADMIN','ORG_ADMIN','AGENCY_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeUser(
            @Parameter(description = "User ID to delete")
            @PathVariable Integer id) {
        userService.removeUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get current authenticated user profile")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    @Operation(summary = "Update current authenticated user profile")
    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateMyProfile(
            @Valid @RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(userService.updateMyProfile(userRequestDTO));
    }
}