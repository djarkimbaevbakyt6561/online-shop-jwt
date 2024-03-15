package peaksoft.service;

import peaksoft.dto.request.AddColourRequest;
import peaksoft.dto.request.ProductInnerPageResponse;
import peaksoft.dto.request.ProductRequest;
import peaksoft.dto.response.BasketProductResponse;
import peaksoft.dto.response.ProductResponse;
import peaksoft.dto.response.SimpleResponse;
import peaksoft.enums.Category;

import java.security.Principal;
import java.util.List;

/**
 * @author Mukhammed Asantegin
 */
public interface ProductService {
    SimpleResponse save(Category category, ProductRequest productRequest);

    SimpleResponse addColours(Long productID, AddColourRequest addColourRequest);

    List<ProductResponse> findAllProducts();

    SimpleResponse addOrRemoveFav(Principal principal, Long productId);
    SimpleResponse addOrRemoveBasket(Principal principal, Long productId, boolean addOrRemove);

    List<ProductResponse> findAllFavProducts(Principal principal);

    ProductInnerPageResponse findById(Long productId);

    List<BasketProductResponse> findBasket(Principal principal);
}