package com.example.reservationsystem.controller;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.reservationsystem.entity.User;
import com.example.reservationsystem.entity.UserLayoutSetting;
import com.example.reservationsystem.repository.UserLayoutSettingRepository;
import com.example.reservationsystem.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
public class UserLayoutSettingController {

    private final UserRepository userRepository;
    private final UserLayoutSettingRepository layoutSettingRepository;

    public UserLayoutSettingController(
        UserRepository userRepository,
        UserLayoutSettingRepository layoutSettingRepository
    ) {
        this.userRepository = userRepository;
        this.layoutSettingRepository = layoutSettingRepository;
    }

    // =========================
    // レイアウト設定画面表示
    // =========================
    @GetMapping("/customize")
    public String customize(
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        User user = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow();

        UserLayoutSetting setting =
            layoutSettingRepository.findByUser(user)
                .orElse(null);

        model.addAttribute(
            "layoutJson",
            setting != null ? setting.getLayoutJson() : "{}"
        );
        
        User currentUser = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow();

            model.addAttribute("user", currentUser);

        return "customize";
    }

    // =========================
    // レイアウト設定保存
    // =========================
    @PostMapping("/customize/save")
    public String saveCustomize(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam String backgroundColor,
        @RequestParam String textColor,
        @RequestParam String fontSize
    ) throws Exception {

    	System.out.println("=== customize save ===");
        System.out.println("backgroundColor = " + backgroundColor);
        System.out.println("textColor = " + textColor);
        System.out.println("fontSize = " + fontSize);
    	
        User user = userRepository
            .findByEmail(userDetails.getUsername())
            .orElseThrow();

        ObjectMapper mapper = new ObjectMapper();

        UserLayoutSetting setting =
            layoutSettingRepository.findByUser(user)
                .orElse(new UserLayoutSetting());

        Map<String, Object> layoutMap;

        if (setting.getLayoutJson() != null && !setting.getLayoutJson().isEmpty()) {
            layoutMap = mapper.readValue(setting.getLayoutJson(), Map.class);
        } else {
            layoutMap = new java.util.HashMap<>();
        }

        // 🔽 ここで「以前のJSONに追加」
        layoutMap.put("backgroundColor", backgroundColor);
        layoutMap.put("textColor", textColor);
        layoutMap.put("fontSize", fontSize);

        setting.setUser(user);
        setting.setLayoutJson(mapper.writeValueAsString(layoutMap));

        layoutSettingRepository.save(setting);

        return "redirect:/dashboard";
    }


    
}
