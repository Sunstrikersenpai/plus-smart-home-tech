package warehouse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.interaction.exeception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.interaction.exeception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.interaction.exeception.SpecifiedProductAlreadyInWarehouseException;

import java.util.Map;

@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            SpecifiedProductAlreadyInWarehouseException.class,
            NoSpecifiedProductInWarehouseException.class,
            ProductInShoppingCartLowQuantityInWarehouse.class
    })
    public Map<String, String> handleWarehouseExceptions(RuntimeException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleOther(Exception ex) {
        return Map.of("error", ex.getMessage());
    }
}
