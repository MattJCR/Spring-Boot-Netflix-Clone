package personal.netflix.clone.app.services;

import personal.netflix.clone.app.entities.User;
import personal.netflix.clone.app.entities.VideoInfo;

import java.util.List;


public interface UserService {

    public void saveUser(User user);
    public List<User> getAllUsers();
    public User updateOrSaveUser(User user);

}
