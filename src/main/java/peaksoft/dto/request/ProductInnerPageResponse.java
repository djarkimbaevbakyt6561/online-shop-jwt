package peaksoft.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peaksoft.enums.Size;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductInnerPageResponse {
    private String image;
    private String name;
    private BigDecimal price;
    private List<Size> sizes;

    public ProductInnerPageResponse(String image, String name, BigDecimal price) {
        this.image = image;
        this.name = name;
        this.price = price;
    }
}