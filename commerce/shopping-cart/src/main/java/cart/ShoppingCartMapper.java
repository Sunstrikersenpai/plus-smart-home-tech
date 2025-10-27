package cart;

import org.mapstruct.Mapper;
import ru.yandex.practicum.interaction.dto.ShoppingCartDto;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {
    ShoppingCartDto toDto(ShoppingCart entity);
}