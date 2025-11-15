package warehouse;

import org.springframework.data.jpa.repository.JpaRepository;
import warehouse.model.OrderBooking;

import java.util.UUID;

public interface OrderBookingRepository extends JpaRepository<OrderBooking, UUID> {
}
