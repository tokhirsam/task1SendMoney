package uz.pdp.task1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.task1.payload.ApiResponse;
import uz.pdp.task1.payload.OutcomeDto;
import uz.pdp.task1.service.IncomeService;
import uz.pdp.task1.service.OutcomeService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/income")
public class IncomeController {
    @Autowired
    IncomeService service;


    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(service.getAll(httpServletRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?>getOne(@PathVariable Integer id, HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(service.getOne(id, httpServletRequest));
    }
}
