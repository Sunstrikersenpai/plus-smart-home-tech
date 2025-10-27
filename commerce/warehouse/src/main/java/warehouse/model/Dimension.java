package warehouse.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dimension {
    private double width;
    private double height;
    private double depth;

    public double getVolume() {
        return width * height * depth;
    }
}