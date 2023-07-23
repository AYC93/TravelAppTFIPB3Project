package travel.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedirectController {

    public class RouteController {
        @RequestMapping("/error")
        public String handleError() {
            return "forward:/";
        }
    }
}
