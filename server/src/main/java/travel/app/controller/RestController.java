package travel.app.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import travel.app.model.PlannerModel.CombinedModel;
import travel.app.model.PlannerModel.Planner;
import travel.app.model.WeatherApiModel.TempInfo;
import travel.app.model.WeatherApiModel.WeatherLoc;
import travel.app.model.WeatherApiModel.WeatherTempInfo;
import travel.app.model.dto.LoginDTO;
import travel.app.service.RepoService.PlannerService;
import travel.app.service.RepoService.UserService;
import travel.app.service.WeatherService.WeatherException;
import travel.app.service.WeatherService.WeatherService;

@Controller
@RequestMapping(path = "/api")
@CrossOrigin
public class RestController {

    @Autowired
    UserService userSvc;

    @Autowired
    PlannerService plannerSvc;

    @Autowired
    WeatherController weatherController;

    @Autowired
    WeatherService weatherSvc;

    @PostMapping(path = "/entry")
    @ResponseBody
    public ResponseEntity<LoginDTO> addOrCheckEmail(@RequestBody Map<String, String> payload) {
        LoginDTO login = new LoginDTO();

        login.setEmail(payload.get("email"));

        String email = login.getEmail();
        System.out.println(email);

        int emailId = userSvc.addLoginSvc(login);

        login.setEmailId(emailId);
        System.out.println("Login Data= " + login);

        return ResponseEntity.ok(login);
    }

    @PostMapping(path = "/entry/post")
    @ResponseBody
    public ResponseEntity<Planner> postFormToRepo(@RequestParam MultiValueMap<String, String> formData,
            @RequestParam(required = false) MultipartFile file) throws IOException {
        // planner.getCity()... after dinner

        Planner p = new Planner();

        // Client to server data
        String dateTimeString = formData.getFirst("date");
        LocalDateTime dateTime = plannerSvc.stringToDateTime(dateTimeString);
        String description = formData.getFirst("description");
        String city = formData.getFirst("city");
        String destinationType = formData.getFirst("destination");
        String email = formData.getFirst("email");
        int emailId = userSvc.getEmailId(email);
        String url = ""; // if no file url empty string

        // File upload & generate url
        if(file != null)
         url = plannerSvc.uploadFileByUser(file).toString();
        
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

        // error when submitting form without uploadfile

        String response = "PID is " + String.valueOf(p.getPid());

        return ResponseEntity.ok(p);
    }

    @GetMapping(path = "/main")
    @ResponseBody
    public ResponseEntity<List<CombinedModel>> getDataFromEmailId(@RequestParam int emailId) {
        List<Planner> plannerList = new LinkedList<>();
        List<CombinedModel> combinedModelList = new LinkedList<>();

        JsonObjectBuilder objBuilder = Json.createObjectBuilder();

        plannerList = plannerSvc.getPlanByUser(emailId);

        for (Planner planner : plannerList) {
            String city = planner.getCity();
            List<WeatherTempInfo> weatherTempInfoList;
            try {
                weatherTempInfoList = weatherSvc.getWeather(city);

                weatherTempInfoList.forEach(weatherTempInfo -> {
                    JsonArrayBuilder weatherArrBuilder = Json.createArrayBuilder();
                    weatherTempInfo.getWeatherInfoList().forEach(d -> weatherArrBuilder.add(Json.createObjectBuilder()
                            .add("main", d.main())
                            .add("description", d.description())
                            .add("icon", d.icon())));
                    objBuilder.add("weather", weatherArrBuilder);

                    JsonObjectBuilder tempObjBuilder = Json.createObjectBuilder();
                    TempInfo tempInfo = weatherTempInfo.getTempInfo();
                    tempObjBuilder.add("temp", tempInfo.temp())
                            .add("tempMax", tempInfo.tempMax())
                            .add("tempMin", tempInfo.tempMin());

                    objBuilder.add("main", tempObjBuilder);

                    JsonObjectBuilder geoObjBuilder = Json.createObjectBuilder();
                    WeatherLoc weatherLoc = weatherTempInfo.getWeatherLoc();
                    tempObjBuilder.add("lat", weatherLoc.lat())
                            .add("lon", weatherLoc.lon());

                    objBuilder.add("geo", geoObjBuilder);

                    CombinedModel combinedModel = new CombinedModel(weatherTempInfo, planner);
                    combinedModelList.add(combinedModel);
                });
            } catch (WeatherException e) {
                System.out.println(Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build().toString());
            }
        }
        return ResponseEntity.ok(combinedModelList);
    }

    @DeleteMapping("/main")
    @ResponseBody
    public ResponseEntity<String> deleteEntryFromRepo(@RequestParam int pid){
        plannerSvc.delPlanByUser(pid);
        
        return ResponseEntity.ok(pid + " is deleted from the repository");
    }

}
