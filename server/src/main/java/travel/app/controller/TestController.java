package travel.app.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import travel.app.model.PlannerModel.Planner;
import travel.app.repository.PlannerRepository;

@RestController
@RequestMapping("/planner")
public class TestController {
    @Autowired
    private PlannerRepository plannerRepository;

   
    @GetMapping("/plans/{user}")
    public ResponseEntity<List<Planner>> getPlansByUser(@PathVariable String user) {
        List<Planner> plannerList = plannerRepository.getPlanByUser(user);
        return ResponseEntity.ok(plannerList);
    }

    @PostMapping("/plans")
    public ResponseEntity<Integer> addPlanByUser(@RequestBody Planner planner) {
        LocalDateTime datetime = planner.getDateTime();
        String description = planner.getDescription();
        String city = planner.getCity();
        String destinationType = planner.getDestinationType();

        int pid = plannerRepository.addPlanByUser(datetime, description, city, destinationType);
        return ResponseEntity.status(HttpStatus.CREATED).body(pid);
    }
}