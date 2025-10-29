package warehouse;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.interaction.api.ShoppingStoreApi;

@FeignClient(name = "shopping-store")
public interface ShoppingStoreClient extends ShoppingStoreApi {
}