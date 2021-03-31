package uz.pdp.task1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.task1.entity.Card;
import uz.pdp.task1.entity.Income;
import uz.pdp.task1.entity.Outcome;
import uz.pdp.task1.payload.ApiResponse;
import uz.pdp.task1.payload.OutcomeDto;
import uz.pdp.task1.repository.CardRepository;
import uz.pdp.task1.repository.IncomeRepository;
import uz.pdp.task1.repository.OutcomeRepository;
import uz.pdp.task1.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OutcomeService {
    @Autowired
    OutcomeRepository outcomeRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    IncomeRepository incomeRepository;


    public ApiResponse add(OutcomeDto dto, HttpServletRequest httpServletRequest) {
        Optional<Card> optionalCardTo = cardRepository.findById(dto.getToCardId());
        if (!optionalCardTo.isPresent()) return new ApiResponse("To card not found", false);
        Optional<Card> optionalCardFrom = cardRepository.findById(dto.getFromCardId());
        if (!optionalCardFrom.isPresent()) return new ApiResponse("From card not found", false);
        Card cardFrom = optionalCardFrom.get();
        Card cardTo = optionalCardTo.get();

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        if (!userName.equals(cardFrom.getUsername())) return
                new ApiResponse("The card you sending from does not belong to you", false);

        Double balance = cardFrom.getBalance();
        Double totalAmount = dto.getAmount() + (dto.getAmount() / 100 * dto.getCommissionPercent());
        if (balance < totalAmount) return new ApiResponse("Balance is not sufficient", false);


        Outcome outcome = new Outcome();
        Income income = new Income();

        outcome.setAmount(dto.getAmount());
        outcome.setCommissionPercent(dto.getCommissionPercent());
        outcome.setFromCardId(cardFrom);
        outcome.setToCardId(cardTo);
        outcome.setDate(new java.util.Date());
        outcomeRepository.save(outcome);

        income.setAmount(dto.getAmount());
        income.setDate(new java.util.Date());
        income.setFromCardId(cardFrom);
        income.setToCardId(cardTo);
        incomeRepository.save(income);


        cardFrom.setBalance(balance - totalAmount);
        cardTo.setBalance(dto.getAmount());
       cardRepository.save(cardFrom);
       cardRepository.save(cardTo);



        return new ApiResponse("Transaction sent successfully", true);
    }


    public List<Outcome> getAll(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        List<Outcome> userOutcome = new ArrayList<>();

        List<Outcome> all = outcomeRepository.findAll();
        for (Outcome outcome : all) {
            if (outcome.getFromCardId().getUsername().equals(userName))
                userOutcome.add(outcome);
        }
        return userOutcome;
    }

    public ApiResponse getOne(Integer id, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        Optional<Outcome> optionalOutcome = outcomeRepository.findById(id);
        if (!optionalOutcome.isPresent()) return new ApiResponse("Bunday Id li outcome transaction mavjud emas", false);
        if (!userName.equals(optionalOutcome.get().getFromCardId().getUsername()))
            return new ApiResponse("Bu ID li transaction sizga tegishli emas", false);
        return new ApiResponse("", true,optionalOutcome.get());
    }

}
