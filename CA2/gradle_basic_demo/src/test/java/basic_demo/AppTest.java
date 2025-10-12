package basic_demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {
    @Test
    void GreetingTest(){
        App app = new App();

        String expected = "\nWelcome to \"Multi-User Chat Application\"!\n";

        assertEquals(expected, app.getGreeting());
    }
}
