package uz.pdp.task1.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.task1.payload.LoginDto;


@RestController
@RequestMapping("/api/report")
public class ReportController {

    @GetMapping
    public HttpEntity<?> getReports(){
        return ResponseEntity.ok("Report sent");
    }

    @PostMapping("/test")
    public HttpEntity<?> addTest(@RequestBody LoginDto loginDto){
        System.out.println(loginDto);
        return ResponseEntity.ok(loginDto);
    }
}
