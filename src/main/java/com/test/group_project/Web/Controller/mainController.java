package com.test.group_project.Web.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class mainController {
    @GetMapping("/")
    public String main(){
        return "tryweb";
    }
    @GetMapping("/search")
    public String search(){
        return "trysearch";
    }
}
