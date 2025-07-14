package git_kkalnane.starbucksbackenv2.global.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "UP";
    }

    @GetMapping("/status")
    public String status() {
        return "Application is running";
    }
}
