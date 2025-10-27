package cart;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.interaction.api.WarehouseApi;

@FeignClient(name = "warehouse")
public interface WarehouseClient extends WarehouseApi {
}
