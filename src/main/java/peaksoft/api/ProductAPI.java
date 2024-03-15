package peaksoft.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.dto.request.AddColourRequest;
import peaksoft.dto.request.ProductInnerPageResponse;
import peaksoft.dto.request.ProductRequest;
import peaksoft.dto.response.BasketProductResponse;
import peaksoft.dto.response.ProductResponse;
import peaksoft.dto.response.SimpleResponse;
import peaksoft.enums.Category;
import peaksoft.service.ProductService;

import java.security.Principal;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductAPI {
    private final ProductService productService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    @GetMapping
    public List<ProductResponse> findAll() {
        return productService.findAllProducts();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/save")
    public SimpleResponse save(
            @RequestParam Category category,
            @RequestBody ProductRequest productRequest
    ) {
        return productService.save(category, productRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{productID}")
    public SimpleResponse addColours(@RequestBody AddColourRequest addColourRequest,
                                     @PathVariable Long productID
    ) {
        return productService.addColours(productID, addColourRequest);
    }

    @PreAuthorize("hasAuthority('CLIENT')")
    @PutMapping("/{productId}")
    public SimpleResponse addOrRemoveFavorite(@PathVariable Long productId, Principal principal) {
        return productService.addOrRemoveFav(principal, productId);
    }
    @PreAuthorize("hasAuthority('CLIENT')")
    @PutMapping("/basket/{productId}")
    public SimpleResponse addOrRemoveBasket(@PathVariable Long productId, Principal principal, @RequestParam boolean addOrRemove) {
        return productService.addOrRemoveBasket(principal, productId, addOrRemove);
    }

    @PreAuthorize("hasAuthority('CLIENT')")
    @GetMapping("/basket")
    public List<BasketProductResponse> findBasket(Principal principal){
        return productService.findBasket(principal);
    }

    @PreAuthorize("hasAuthority('CLIENT')")
    @GetMapping("/favorite")
    public List<ProductResponse> findFav(Principal principal) {
        return productService.findAllFavProducts(principal);
    }

    @PreAuthorize("hasAuthority('CLIENT')")
    @GetMapping("/{productId}")
    public ProductInnerPageResponse findById(@PathVariable Long productId) {
        return productService.findById(productId);
    }


}