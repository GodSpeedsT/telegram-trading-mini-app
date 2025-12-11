package com.app.tradeguess.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class ChartController {

    @GetMapping("/chart")
    public String chart() {

    }

}
