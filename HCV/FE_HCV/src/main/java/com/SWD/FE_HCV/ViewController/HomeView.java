package com.SWD.FE_HCV.ViewController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HomeView {
@GetMapping("/")
public String homeIndex() {
    return "home";
}
@GetMapping("/home")
public String homeView() {
    return "home";
}

@GetMapping("/profile")
public String getProfile() {
    return "profile";
}

@GetMapping("/exam")
public String getExam() {
    return "exam";
}

@GetMapping("/forum")
public String getForum() {
    return "forum";
}

}
