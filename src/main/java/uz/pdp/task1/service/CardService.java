package uz.pdp.task1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.task1.entity.Card;
import uz.pdp.task1.payload.ApiResponse;
import uz.pdp.task1.payload.CardDto;
import uz.pdp.task1.repository.CardRepository;
import uz.pdp.task1.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    @Autowired
    CardRepository repository;
    @Autowired
    JwtProvider jwtProvider;

    public ApiResponse add(CardDto dto, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        Card card = new Card();
        card.setUsername(userName);
        card.setCardNumber(dto.getCardNumber());
        card.setExpiredDate(dto.getExpiredDate());
        repository.save(card);
        return new ApiResponse("New Card added", true);
    }

    public ApiResponse edit(Integer id, CardDto dto, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        Optional<Card> optionalCard = repository.findById(id);
        if (!optionalCard.isPresent()) return new ApiResponse("Bunday ID li card mavjud emas", false);
        if (!userName.equals(optionalCard.get().getUsername()))
            return new ApiResponse("This card does not belong to you", false);
        Card card = optionalCard.get();
        card.setCardNumber(dto.getCardNumber());
        card.setExpiredDate(dto.getExpiredDate());
        repository.save(card);
        return new ApiResponse("Card edited", true);
    }

    public ApiResponse delete(Integer id, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        try {
            Optional<Card> optionalCard = repository.findById(id);
            if (!optionalCard.isPresent()) return new ApiResponse("Bunday ID li card mavjud emas", false);
            if (!userName.equals(optionalCard.get().getUsername()))
                return new ApiResponse("This card does not belong to you", false);
            repository.deleteById(id);
            return new ApiResponse("Card deleted", true);
        } catch (Exception e) {
            return new ApiResponse("Card mavjud emas", false);
        }
    }

    public List<Card> getCard(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        List<Card> userCards = new ArrayList<>();

        List<Card> all = repository.findAll();
        for (Card card : all) {
            if (card.getUsername().equals(userName))
                userCards.add(card);
        }
        return userCards;
    }


    public Card getOne(Integer id, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        Optional<Card> optionalCard = repository.findById(id);
        if (!optionalCard.isPresent()) return null;
        if (!userName.equals(optionalCard.get().getUsername()))
            return null;
        return optionalCard.orElse(null);
    }
}
