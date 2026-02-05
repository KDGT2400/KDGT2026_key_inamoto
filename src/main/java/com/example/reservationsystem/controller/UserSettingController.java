package com.example.reservationsystem.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/settings")
public class UserSettingController {

    @GetMapping
    public String getSettings(Authentication auth) {
    	return "redirect:/settings";
        // userId取得 → DBからsettings_json返す
    }

    @PostMapping
    public void saveSettings(@RequestBody String settingsJson,
                             Authentication auth) {
        // userId + settingsJsonを保存
    }
    
    
}
