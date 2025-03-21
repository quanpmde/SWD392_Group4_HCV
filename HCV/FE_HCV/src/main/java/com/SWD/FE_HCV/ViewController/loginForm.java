package com.SWD.FE_HCV.ViewController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class loginForm {
@GetMapping("/login")
public String login() {
    return "login";
}

}
