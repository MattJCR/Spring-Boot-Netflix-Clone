package personal.netflix.clone.app.ServicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.netflix.clone.app.entities.User;
import personal.netflix.clone.app.repositories.UserRepository;
import personal.netflix.clone.app.services.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user){
        userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User updateOrSaveUser(User user){
        if(user.getId() != null){
            Optional<User> optionalUser = userRepository.findById(user.getId());
            if(optionalUser.isPresent()){
                User userToUpdate = optionalUser.get();
                userToUpdate.setName(user.getName());
                user.getVideoInfos().forEach(vi -> userToUpdate.getVideoInfos().add(vi));
                userToUpdate.getVideoInfos().forEach(vi -> System.out.println("VI_FileName: " + vi.getFileName()));
                return userRepository.save(userToUpdate);
            }
        }
        return userRepository.save(user);
    }

}
