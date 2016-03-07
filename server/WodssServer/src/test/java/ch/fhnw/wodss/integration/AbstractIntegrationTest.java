package ch.fhnw.wodss.integration;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.fhnw.wodss.WodssServer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WodssServer.class)
@WebIntegrationTest
public class AbstractIntegrationTest {

}
