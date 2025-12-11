package com.app.tradeguess.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class StatsController {

    @PostMapping("/guess")
    public String guess() {

    }

    @GetMapping("/me/stats")
    public String stats() {

    }

}
