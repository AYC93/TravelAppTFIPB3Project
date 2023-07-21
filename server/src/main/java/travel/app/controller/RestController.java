package travel.app.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import travel.app.model.PlannerModel.Planner;
import travel.app.model.dto.LoginDTO;
import travel.app.service.RepoService.PlannerService;
import travel.app.service.RepoService.UserService;

@Controller
@RequestMapping(path = "/api")
@CrossOrigin
public class RestController {

    @Autowired
    UserService userSvc;

    @Autowired
    PlannerService plannerSvc;

    @PostMapping(path = "/entry")
    @ResponseBody
    public ResponseEntity<LoginDTO> addOrCheckEmail(@RequestBody Map<String, String> payload, HttpSession sess) {
        LoginDTO loginData = new LoginDTO();

        loginData.setEmail(payload.get("email"));

        String email = loginData.getEmail();
        System.out.println(email);

        int emailId = userSvc.addLoginSvc(loginData);

        loginData.setEmailId(emailId);

        sess.setAttribute("loginData", loginData);

        return ResponseEntity.ok(loginData);
    }

    @PostMapping(path = "/entry/post")
    @ResponseBody
    public ResponseEntity<Planner> postFormToRepo(@RequestPart MultiValueMap<String, String> formField,
            @RequestPart("file") MultipartFile file, HttpSession sess) throws IOException {
        // planner.getCity()... after dinner

        Planner p = new Planner();

        LoginDTO loginData = (LoginDTO) sess.getAttribute("loginData");

        // Client to server data
        String dateTimeString = formField.getFirst("date");
        LocalDateTime dateTime = plannerSvc.stringToDateTime(dateTimeString);
        String description = formField.getFirst("description");
        String city = formField.getFirst("city");
        String destinationType = formField.getFirst("destination");
        String email = loginData.getEmail();
        int emailId = userSvc.getEmailId(email);

        // File upload & generate url
        String url = plannerSvc.uploadFileByUser(file).toString();
        // Form upload & generate pid
        int pid = plannerSvc.addPlanByUser(dateTime, description, city, destinationType, email, url);

        // Setup planner
        p.setCity(city);
        p.setDateTime(dateTime);
        p.setDescription(description);
        p.setDestinationType(destinationType);
        p.setEmailId(emailId);
        p.setUrl(url);
        p.setPid(pid);

        sess.setAttribute("planner", p);

        return ResponseEntity.ok(p);
    }
}
