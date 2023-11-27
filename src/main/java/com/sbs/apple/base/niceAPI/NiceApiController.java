package com.sbs.apple.base.niceAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NiceApiController {
    private final NiceApiTokenService niceApiTokenService;

    @Autowired
    public NiceApiController(NiceApiTokenService niceApiTokenService) {
        this.niceApiTokenService = niceApiTokenService;
    }

    @PostMapping("/getNiceApiToken")
    @ResponseBody
    public ResponseEntity<String> getNiceApiToken() {
        String accessToken = niceApiTokenService.getAccessToken();

        return ResponseEntity.ok(accessToken);
    }
}
