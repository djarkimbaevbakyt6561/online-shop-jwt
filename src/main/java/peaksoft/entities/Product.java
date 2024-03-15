package peaksoft.entities;

import jakarta.persistence.*;
import lombok.*;
import peaksoft.enums.Category;
import peaksoft.enums.Size;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", allocationSize = 1)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Category category;
    private BigDecimal price;
    private String image;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Size> sizes;
    @ElementCollection
    private List<String> colours;
    private long quantity;

    public void addColour(String colour) {
        if (this.colours == null) this.colours = new ArrayList<>();
        this.colours.add(colour);
    }
}