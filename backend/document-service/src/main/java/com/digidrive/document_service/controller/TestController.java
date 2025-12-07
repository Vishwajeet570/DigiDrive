package com.digidrive.document_service.controller;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is PUBLIC — no token required!";
    }

    @GetMapping("/secure")
    public String secureEndpoint() {
        return "This is SECURE — valid JWT token required!";
    }
}
