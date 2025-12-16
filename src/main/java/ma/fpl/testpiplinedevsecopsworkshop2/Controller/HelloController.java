package ma.fpl.testpiplinedevsecopsworkshop2.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {

        String message = "Hello World";
        String message2 = "Hello World"; // duplication inutile

        if (true) { // condition inutile
            return message;
        }
        return message2;
    }
}
