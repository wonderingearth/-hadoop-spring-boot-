package com.test.group_project.Web.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class directorController {
    @GetMapping("/director")
    public String director(){
        return "trytabledirector";
    }
}
