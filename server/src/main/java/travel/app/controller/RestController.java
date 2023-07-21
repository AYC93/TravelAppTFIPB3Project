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
import org.springframework.web.bind.annotation.RequestParam;
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
        LoginDTO login = new LoginDTO();

        login.setEmail(payload.get("email"));

        String email = login.getEmail();
        System.out.println(email);

        int emailId = userSvc.addLoginSvc(login);

        login.setEmailId(emailId);
        System.out.println(emailId);

        sess.setAttribute("email", email);
        sess.setAttribute("emailId", emailId);
        System.out.println("Login Data= " + login);

        return ResponseEntity.ok(login);
    }

    @PostMapping(path = "/entry/post")
    @ResponseBody
    public ResponseEntity<Planner> postFormToRepo(@RequestParam MultiValueMap<String, String> formData,
            @RequestParam(required = false) MultipartFile file, HttpSession sess) throws IOException {
        // planner.getCity()... after dinner

        Planner p = new Planner();

        // Client to server data
        String dateTimeString = formData.getFirst("date");
        LocalDateTime dateTime = plannerSvc.stringToDateTime(dateTimeString);
        String description = formData.getFirst("description");
        String city = formData.getFirst("city");
        String destinationType = formData.getFirst("destination");
        String email = (String) sess.getAttribute("emailId");
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
