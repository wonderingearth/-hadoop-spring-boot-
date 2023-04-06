package com.test.group_project.Web.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class pieController {
    @GetMapping("/pie")
    public String pie(){
        return "trypie";
    }
}
