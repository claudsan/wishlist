package br.com.claudsan.wishlist.controllers;

import br.com.claudsan.wishlist.controllers.apidocs.CustomerWishlistControllerDocs;
import br.com.claudsan.wishlist.document.Product;
import br.com.claudsan.wishlist.document.WishList;
import br.com.claudsan.wishlist.dto.ProductDTO;
import br.com.claudsan.wishlist.dto.WishListRequestDTO;
import br.com.claudsan.wishlist.dto.WishListUpdateRequestDTO;
import br.com.claudsan.wishlist.service.WishListService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/wishlist")
@AllArgsConstructor
public class CustomerWishlistController implements CustomerWishlistControllerDocs {

    private final WishListService service;
    private final ModelMapper mapper;


    @Override
    @GetMapping("/{wishlistId}")
    public Mono<WishList> getWishListById(@PathVariable String wishlistId) {
        return service.getWishListById(wishlistId);
    }

    @Override
    @PutMapping("/{wishlistId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<WishList> updateWishListDataById(@PathVariable String wishlistId, @Valid @RequestBody WishListUpdateRequestDTO wishListData) {
        return service.updateWishListData(WishList.builder().id(wishlistId).title(wishListData.getTitle()).build());
    }

    @Override
    @GetMapping("/customer/{idCustomer}")
    public Flux<WishList> getWishListByCustomerId(@PathVariable Long idCustomer) {
        return service.getWishListByCustomer(idCustomer);
    }

    @Override
    @DeleteMapping("/{wishlistId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<Void> deleteWishlist(@PathVariable String wishlistId){
        return service.deleteWishList(wishlistId);
    }

    @Override
    @GetMapping("/customer/{customerId}/product/{productId}")
    public Flux<WishList> getProductInWishlist(@PathVariable Long customerId, @PathVariable Long productId){
       return service.getWishListByCustomerAndProductId(customerId, productId);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<WishList> createWishlist(@Valid @RequestBody WishListRequestDTO wishList){
        return service.createWishList(mapper.map(wishList, WishList.class));
    }

    @Override
    @PutMapping("/{wishlistId}/product")
    @ResponseStatus(code = HttpStatus.OK)
    public Mono<WishList> addProduct(@PathVariable String wishlistId,@Valid @RequestBody ProductDTO product){
        return service.addProductInWishList(wishlistId, mapper.map(product, Product.class));
    }

    @Override
    @PatchMapping("/{wishlistId}/product/{productId}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public Mono<WishList> removeProductInWishlist(@PathVariable String wishlistId, @PathVariable Long productId){
       return service.removeListProductInWishlist(wishlistId, productId);
    }

    @Override
    @GetMapping("/{wishlistId}/product/{productId}")
    public Mono<Product> getProductInWishlist(@PathVariable String wishlistId, @PathVariable Long productId){
        return service.findProductInWishlist(wishlistId, productId);
    }


}
