package peaksoft.dto.request;

import lombok.Getter;
import lombok.Setter;
import peaksoft.enums.Size;
import peaksoft.entities.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Mukhammed Asantegin
 */

@Getter @Setter
public class ProductRequest {
    private String name;
    private BigDecimal price;
    private long quantity;
    private List<Size> sizes;
    private String image;
    private String colour;

    public Product build(){
        Product product = new Product();
        product.setName(this.name);
        product.setPrice(this.price);
        product.setImage(this.image);
        product.setQuantity(this.quantity);
        product.setSizes(this.sizes);
        product.addColour(this.colour);
        return product;
    }
}