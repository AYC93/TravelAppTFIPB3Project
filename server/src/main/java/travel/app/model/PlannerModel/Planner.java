package travel.app.model.PlannerModel;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Planner {
    private int pid;
    private int emailId;
    private LocalDateTime dateTime;
    private String description;
    private String city;
    private String destinationType;
    private String url;

    public static Planner createFromSQLRowSet(SqlRowSet rs) {
        return new Planner(rs.getInt("pid"),
                rs.getInt("email_id"),
                toLocalDateTime(rs.getTimestamp("datetime")),
                rs.getString("description"),
                rs.getString("city"),
                rs.getString("destination"),
                rs.getString("url")
                );}

    private static LocalDateTime toLocalDateTime(Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        }

    }
