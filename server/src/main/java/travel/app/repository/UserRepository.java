package travel.app.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import travel.app.model.dto.LoginDTO;

import static travel.app.repository.Queries.*;

@Repository
public class UserRepository {
    
    @Autowired JdbcTemplate jdbcTemplate;

    public void addLogin(LoginDTO loginDTO){
        String email = loginDTO.getEmail();
        Optional<LoginDTO> checkEmail = jdbcTemplate.query(SQL_FIND_EMAIL,
            rs -> {
                if (!rs.next())
                return Optional.empty();
                
                LoginDTO l = new LoginDTO();
                return Optional.of(l);
            },
            email
        );
        if (checkEmail.isEmpty()){
            jdbcTemplate.update(SQL_INSERT_LOGIN, email);
        }
    }
}
