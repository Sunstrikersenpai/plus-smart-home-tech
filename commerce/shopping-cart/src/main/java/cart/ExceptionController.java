package cart;

import feign.FeignException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.interaction.exeception.NoProductsInShoppingCartException;
import ru.yandex.practicum.interaction.exeception.NotAuthorizedUserException;

import java.util.Map;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(NotAuthorizedUserException.class)
    public Map<String, String> handleUnauthorized(NotAuthorizedUserException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(NoProductsInShoppingCartException.class)
    public Map<String, String> handleNoProducts(NoProductsInShoppingCartException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(FeignException.BadRequest.class)
    public Map<String, String> handleFeignBadRequest(FeignException.BadRequest ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Map<String, String> handleGeneric(Exception ex) {
        return Map.of("error", ex.getMessage());
    }
}