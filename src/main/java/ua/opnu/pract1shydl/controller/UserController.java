package ua.opnu.pract1shydl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import ua.opnu.pract1shydl.dto.UserDTO;
import ua.opnu.pract1shydl.model.User;
import ua.opnu.pract1shydl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping
  public User create(@RequestBody UserDTO dto) {
    return userService.createUser(dto);
  }

  //@GetMapping
  //public List<User> list() {
  //  return userService.getAllUsers();
  //}

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('USER')")
  public User getUser(@PathVariable Long id) {
    return userService.getUser(id);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('USER')")
  public User updateUser(@PathVariable Long id, @RequestBody User user) {
    return userService.updateUser(id, user);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('USER')")
  public void deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
  }
}