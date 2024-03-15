package peaksoft.service.impls;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.dto.request.AddColourRequest;
import peaksoft.dto.request.ProductInnerPageResponse;
import peaksoft.dto.request.ProductRequest;
import peaksoft.dto.response.BasketProductResponse;
import peaksoft.dto.response.ProductResponse;
import peaksoft.dto.response.SimpleResponse;
import peaksoft.enums.Category;
import peaksoft.entities.Product;
import peaksoft.entities.User;
import peaksoft.repository.ProductRepository;
import peaksoft.repository.UserRepository;
import peaksoft.service.ProductService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Mukhammed Asantegin
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepo;
    private final UserRepository userRepository;

    @Override
    public SimpleResponse save(Category category, ProductRequest productRequest) {
        Product buildProduct = productRequest.build();
        buildProduct.setCategory(category);
        productRepo.save(buildProduct);
        return new SimpleResponse(HttpStatus.OK, "Successfully saved product with name: "+buildProduct.getName());
    }

    @Override @Transactional
    public SimpleResponse addColours(Long productID, AddColourRequest addColourRequest) {
        Product product = productRepo.findById(productID).orElseThrow(() -> new NoSuchElementException("Product with id: " + productID + "not found"));
        for (String colour : addColourRequest.colours()) {
            product.addColour(colour);
        }
        return new SimpleResponse(HttpStatus.OK, "Success add colours");

    }

    @Override
    public List<ProductResponse> findAllProducts() {
        return productRepo.findAllProducts();

    }

    @Override @Transactional
    public SimpleResponse addOrRemoveFav(Principal principal, Long productId) {
        User loginUser = userRepository.getByEmail(principal.getName());
        Product product = productRepo.findById(productId).orElseThrow(() -> new NoSuchElementException("Product with id: " + productId + "not found"));

        if (loginUser.getFavoriteProducts().contains(product)){
            loginUser.getFavoriteProducts().remove(product);
            return SimpleResponse.builder().httpStatus(HttpStatus.ACCEPTED).message("Product removed from favorite").build();
        }
        loginUser.getFavoriteProducts().add(product);
        return SimpleResponse.builder().httpStatus(HttpStatus.ACCEPTED).message("Product added to favorite").build();
    }

    @Override @Transactional
    public SimpleResponse addOrRemoveBasket(Principal principal, Long productId, boolean addOrRemove) {
        User loginUser = userRepository.getByEmail(principal.getName());
        Product product = productRepo.findById(productId).orElseThrow(() -> new NoSuchElementException("Product with id: " + productId + "not found"));

        if (addOrRemove){
            loginUser.getBasketProducts().add(product);
            return SimpleResponse.builder().httpStatus(HttpStatus.ACCEPTED).message("Product added to basket").build();
        }
        loginUser.getBasketProducts().remove(product);
        return SimpleResponse.builder().httpStatus(HttpStatus.ACCEPTED).message("Product removed from basket").build();
    }

    @Override
    public List<ProductResponse> findAllFavProducts(Principal principal) {
        return productRepo.findAllFavProducts(principal.getName());
    }

    @Override
    public ProductInnerPageResponse findById(Long productId) {
        ProductInnerPageResponse product = productRepo.findProudctById(productId);
        product.setSizes(productRepo.getSizes(productId));
        return product;

    }

    @Override
    public List<BasketProductResponse> findBasket(Principal principal) {
        List<ProductResponse> products = productRepo.findAllBasketProducts(principal.getName());
        List<BasketProductResponse> basketProductResponseList = new ArrayList<>();
        for (ProductResponse product : products) {
            Integer countOfProductInBasket = productRepo.findCountOfProductInBasket(product.id());
            basketProductResponseList.add(BasketProductResponse.builder().count(countOfProductInBasket).data(product).build());
        }
        return basketProductResponseList;
    }
}