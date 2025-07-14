package git_kkalnane.starbucksbackenv2.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
public class DatabaseTestController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/db-test")
    public String testDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                return "Database connection: SUCCESS";
            } else {
                return "Database connection: FAILED - Connection is null or closed";
            }
        } catch (SQLException e) {
            return "Database connection: FAILED - " + e.getMessage();
        }
    }
}
