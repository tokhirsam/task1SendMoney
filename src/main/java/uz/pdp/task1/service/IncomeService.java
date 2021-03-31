package uz.pdp.task1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.task1.entity.Income;
import uz.pdp.task1.entity.Outcome;
import uz.pdp.task1.payload.ApiResponse;
import uz.pdp.task1.repository.CardRepository;
import uz.pdp.task1.repository.IncomeRepository;
import uz.pdp.task1.repository.OutcomeRepository;
import uz.pdp.task1.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IncomeService {
    @Autowired
    OutcomeRepository outcomeRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    IncomeRepository incomeRepository;


    public List<Income> getAll(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        List<Income> userIncome = new ArrayList<>();

        List<Income> all = incomeRepository.findAll();
        for (Income income : all) {
            if (income.getToCardId().getUsername().equals(userName))
                userIncome.add(income);
        }
        return userIncome;
    }

    public ApiResponse getOne(Integer id, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        Optional<Income> optionalIncome = incomeRepository.findById(id);
        if (!optionalIncome.isPresent()) return new ApiResponse("Bunday Id li income transaction mavjud emas", false);
        if (!userName.equals(optionalIncome.get().getToCardId().getUsername()))
            return new ApiResponse("Bu ID li income transaction sizga tegishli emas", false);
        return new ApiResponse("", true,optionalIncome.get());
    }
}
