package travel.app.repository;

public class Queries {

        public static final String SQL_FIND_EMAIL = """
                        select * from user where email = ?
                        """;

        public static final String SQL_INSERT_LOGIN = """
                        INSERT INTO user (email) VALUES (?)
                        """;

        public static final String SQL_GET_TRAVELPLAN = """
                        SELECT u.email, tp.datetime, tp.description, tp.city, tp.destination AS dest
                        FROM user u
                        JOIN travelplan tp ON u.email_id = tp.email_id
                        WHERE u.email = ?
                        ORDER BY tp.datetime ASC;
                        """;

        public static final String SQL_FIND_EMAILID="""
                        select email_id as emailId from user where email = ?
                        """;

        public static final String SQL_ADD_TRAVELPLAN="""
                        INSERT INTO planner (datetime, description, city, destination, email_id) VALUES (?, ?, ?, ?, ?)
                        """;
}
