package travel.app.service.RepoService;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import travel.app.model.PlannerModel.Planner;
import travel.app.repository.PlannerRepository;

@Service
public class PlannerService {
    @Autowired
    private PlannerRepository plannerRepo;

    // File upload to S3
    public URL uploadFileByUser(MultipartFile file) throws IOException{
        return plannerRepo.uploadFileByUser(file);
    }

    // Form upload to MySQL
    public int addPlanByUser(LocalDateTime datetime, String description, String city, String destinationType, String email, String url){
        return plannerRepo.addPlanByUser(datetime, description, city, destinationType, email, url);
    }

    public List<Planner> getPlanByUser(int emailId) {
        return plannerRepo.getPlanByUser(emailId);
    }

    public void delPlanByUser(int pid){
        plannerRepo.delPlanByUser(pid);
    }

    // DateTimeFormatter string to localdatetime
    public LocalDateTime stringToDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        return dateTime;
    }
}
