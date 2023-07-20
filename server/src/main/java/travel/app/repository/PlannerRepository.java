package travel.app.repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import travel.app.model.PlannerModel.Planner;

import static travel.app.repository.Queries.*;

@Repository
public class PlannerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepo;

    // mongoTemplate to use same email as unique id, to add into each function

    // display all data in table
    public List<Planner> getPlanByUser(String user) {
        List<Planner> plannerList = new LinkedList<>();

        user = "abc@gmail.com";
        // replace email with login auth email in the future
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_TRAVELPLAN, user);
        while (rs.next()) {
            int pid = rs.getInt("pid");
            Planner planner = Planner.createFromSQLRowSet(rs);
            Document doc = getDocumentByPid(pid);
            planner.setDocument(doc);
            plannerList.add(planner);
        }
        return plannerList;
    }

    public int addPlanByUser(LocalDateTime datetime, String description, String city, String destinationType) {

        // replace email with login auth email in the future
        String email = "abc@gmail.com";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(SQL_ADD_TRAVELPLAN, new String[] { "pid" });
            ps.setTimestamp(1, Timestamp.valueOf(datetime));
            ps.setString(2, description);
            ps.setString(3, city);
            ps.setString(4, destinationType);
            ps.setInt(5, userRepo.getEmailId(email));
            ps.setString(6, email);
            return ps;
        }, keyHolder);

        int pid = keyHolder.getKey().intValue();

        return pid;
    }

    public void delPlanByUser(int pid) {

    }

 

    private Document getDocumentByPid(int pid) {
        Query query = new Query(Criteria.where("pid").is(pid));
        return mongoTemplate.findOne(query, Document.class, "document");
    }

}
