package travel.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import travel.app.model.dto.LoginDTO;
import travel.app.service.RepoService.UserService;

@Controller
@RequestMapping(path = "/api")
@CrossOrigin
public class RestController {

    @Autowired
    UserService userSvc;

    @PostMapping(path = "/entry")
    @ResponseBody
    public ResponseEntity<LoginDTO> addOrCheckEmail(@RequestBody Map<String, String> payload) {
        LoginDTO loginData = new LoginDTO();

        loginData.setEmail(payload.get("email"));

        String email = loginData.getEmail();
        System.out.println(email);

        int emailId = userSvc.addLoginSvc(loginData);

        loginData.setEmailId(emailId);

        return ResponseEntity.ok(loginData);
    }
}
