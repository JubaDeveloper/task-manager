package io.github.jubadeveloper;

import io.github.jubadeveloper.configuration.AppConfig;
import io.github.jubadeveloper.configuration.DatabaseConfig;
import io.github.jubadeveloper.core.domain.User;
import io.github.jubadeveloper.core.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, DatabaseConfig.class})
@DirtiesContext
public class DatasourceTest {
    @Autowired
    public DataSource dataSource;

    @Autowired
    public UserRepository userRepository;

    @Test
    public void shouldLoadDataSource () throws SQLException {
        Assertions.assertThat(dataSource).isNotNull();
        Connection connection = dataSource.getConnection();
        ResultSet resultSet = connection.prepareStatement("SELECT * FROM test").executeQuery();
        resultSet.next();
        Assertions.assertThat(resultSet.getString("name")).isEqualTo("JUBA");
    }

    @Test
    public void shouldRetrieveOneUser () {
        User user = userRepository.findById(10L).orElse(null);
        Assertions.assertThat(user).isNotNull();
    }
}
