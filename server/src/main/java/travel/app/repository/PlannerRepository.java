package travel.app.repository;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.mongodb.core.MongoTemplate;
// import org.springframework.data.mongodb.core.query.Criteria;
// import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import travel.app.model.PlannerModel.Planner;

import static travel.app.repository.Queries.*;

@Repository
public class PlannerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // @Autowired
    // private MongoTemplate mongoTemplate;

    @Autowired
    private AmazonS3 s3;

    @Autowired
    private UserRepository userRepo;

    // mongoTemplate to use same email as unique id, to add into each function

    // display all data in table
    public List<Planner> getPlanByUser(int emailId) {
        List<Planner> plannerList = new LinkedList<>();

        // replace email with login auth email in the future
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_TRAVELPLAN, emailId);
        while (rs.next()) {
            Planner planner = Planner.createFromSQLRowSet(rs);
            plannerList.add(planner);
        }
        return plannerList;
    }

    // String email to be input here
    public int addPlanByUser(LocalDateTime dateTime, String description, String city, String destinationType,
            String email, String url) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        System.out.println("email Id obtained from repo: " + userRepo.getEmailId(email));

        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(SQL_ADD_TRAVELPLAN, Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1, Timestamp.valueOf(dateTime));
            ps.setString(2, description);
            ps.setString(3, city);
            ps.setString(4, destinationType);
            ps.setString(5, url);
            ps.setInt(6, userRepo.getEmailId(email));
            return ps;
        }, keyHolder);

        int pid = keyHolder.getKey().intValue();
        System.out.println("PID is " + pid);

        return pid;
    }

    // Upload via S3
    public URL uploadFileByUser(MultipartFile file) throws IOException {
        // Custom metadata
        Map<String, String> fileData = new HashMap<>();
        fileData.put("filename", file.getOriginalFilename());
        fileData.put("upload-date", (new Date()).toString());

        ObjectMetadata md = new ObjectMetadata();
        md.setContentType(file.getContentType());
        md.setContentLength(file.getSize());
        md.setUserMetadata(fileData);

        String fileKey = "file/%s".formatted(UUID.randomUUID().toString()
                .substring(0, 8));

        PutObjectRequest pReq = new PutObjectRequest(BUCKETNAME, fileKey,
                file.getInputStream(), md);
        pReq = pReq.withCannedAcl(CannedAccessControlList.PublicRead);

        try {
            PutObjectResult pRes = s3.putObject(pReq);
        } catch (AmazonClientException e) {
            throw new IOException("Upload failed.");
        }

        return s3.getUrl(BUCKETNAME, fileKey);
    }

    public void delPlanByUser(int pid) {
        String url = jdbcTemplate.queryForObject(SQL_FIND_URL, String.class, pid);
        if (!url.isEmpty()) {
            String filename = url.substring(url.lastIndexOf("file"));
            System.out.println(filename);
            s3.deleteObject(BUCKETNAME, filename);
        }
        jdbcTemplate.update(SQL_DEL_TRAVELPLAN, pid);
        System.out.println("Entry" + pid + " is deleted");
    }

    // get file to view
    // private MultipartFile getFileByPid(int pid) {
    // Query query = new Query(Criteria.where("pid").is(pid));
    // return mongoTemplate.findOne(query, MultipartFile.class, "file");
    // }

}
