package travel.app.model.dto;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginDTO{
    private String email;
    private int emailId;

     public static LoginDTO createFromSQLRowSet(SqlRowSet rs) {
        return new LoginDTO(rs.getString("email"),
                    rs.getInt("emailId"));
    }


}
