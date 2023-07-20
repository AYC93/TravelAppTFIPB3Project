package travel.app.service.RepoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import travel.app.model.dto.LoginDTO;
import travel.app.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public int addLoginSvc(LoginDTO loginDTO) {
        return userRepo.addLogin(loginDTO);
    }
}
