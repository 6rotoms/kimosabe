package kimosabe.api;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class AbstractBaseIntegrationTest {
    @Autowired
    public TestRestTemplate restTemplate;

    @LocalServerPort
    public int randomServerPort;
    public String baseUrl;

    @Autowired
    Flyway fw;

    public void cleanUpDb() {
        fw.clean();
        fw.migrate();
    }
}
