package travel.app.model.PlannerModel;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.bson.Document;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Planner {
    private int pid;
    private String email;
    private LocalDateTime dateTime;
    private String description;
    private String city;
    private String destinationType;
    private Document document;

    public static Planner createFromSQLRowSet(SqlRowSet rs) {
        return new Planner(rs.getInt("pid"),
                rs.getString("email"),
                toLocalDateTime(rs.getTimestamp("datetime")),
                rs.getString("description"),
                rs.getString("city"),
                rs.getString("dest"),
                null);
    }

    private static LocalDateTime toLocalDateTime(Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        }
    
    public void setDocument(Document document){
        this.document = document;
    }

    }
