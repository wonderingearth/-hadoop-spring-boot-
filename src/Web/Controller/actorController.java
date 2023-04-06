package com.test.group_project.Web.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class actorController {
    @GetMapping("/actor")
    public String actor(){
        return "trytableactor";
    }
}
