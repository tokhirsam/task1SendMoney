package uz.pdp.task1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.task1.entity.Card;
import uz.pdp.task1.payload.ApiResponse;
import uz.pdp.task1.payload.CardDto;
import uz.pdp.task1.security.JwtProvider;
import uz.pdp.task1.service.CardService;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/card")
@RestController
public class CardController {
    @Autowired
    CardService service;
    @Autowired
    JwtProvider jwtProvider;




    @PostMapping
    public HttpEntity<ApiResponse> add(@RequestBody CardDto dto, HttpServletRequest httpServletRequest){
        ApiResponse apiResponse = service.add(dto,httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess()?201:209).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(service.getCard(httpServletRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card>getOne(@PathVariable Integer id, HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(service.getOne(id, httpServletRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> edit(@RequestBody CardDto dto,
                                            @PathVariable Integer id, HttpServletRequest httpServletRequest){
        ApiResponse apiResponse = service.edit(id, dto, httpServletRequest);
        if (apiResponse.isSuccess()){
            return ResponseEntity.status(202).body(apiResponse);
        }else {
            return ResponseEntity.status(409).body(apiResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id, HttpServletRequest httpServletRequest){
        ApiResponse response = service.delete(id, httpServletRequest);
        return ResponseEntity.status(response.isSuccess()?202:409).body(response);
    }

//    @GetMapping
//    public String getUsername(HttpServletRequest httpServletRequest){
//        String token = httpServletRequest.getHeader("Authorization");
//        token = token.substring(7);
//        String userName = jwtProvider.getUserNameFromToken(token);
//        return userName;
//    }


}
