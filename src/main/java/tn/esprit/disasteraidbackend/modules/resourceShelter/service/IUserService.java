package tn.esprit.disasteraidbackend.modules.resourceShelter.service;

import java.util.List;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.User;

public interface IUserService {
    User addUser(User user);
    User getUser(Integer id);
    User updateUser(User user);
    void deleteUser(Integer id);
    List<User> getAllUsers();
    List<User> addAllUsers(List<User> users);
}