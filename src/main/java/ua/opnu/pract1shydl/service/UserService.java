package ua.opnu.pract1shydl.service;

import lombok.RequiredArgsConstructor;
import ua.opnu.pract1shydl.dto.UserDTO;
import ua.opnu.pract1shydl.model.User;
import ua.opnu.pract1shydl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public User createUser(UserDTO userDTO) {
    User user = new User();
    user.setUsername(userDTO.getUsername());
    user.setEmail(userDTO.getEmail());
    return userRepository.save(user);
  }
  public User getUser(Long id) { return userRepository.findById(id).orElse(null); }
  public User updateUser(Long id, User updatedUser) {
    User user = getUser(id);
    if (user != null) {
      user.setUsername(updatedUser.getUsername());
      user.setEmail(updatedUser.getEmail());
      return userRepository.save(user);
    }
    return null;
  }

  //public List<User> getAllUsers() {
  //  return userRepository.findAll();
  //}
  public void deleteUser(Long id) { userRepository.deleteById(id); }
}