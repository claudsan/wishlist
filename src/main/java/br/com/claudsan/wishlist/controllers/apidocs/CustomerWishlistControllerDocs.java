package br.com.claudsan.wishlist.controllers.apidocs;

import br.com.claudsan.wishlist.document.Product;
import br.com.claudsan.wishlist.document.WishList;
import br.com.claudsan.wishlist.dto.ProductDTO;
import br.com.claudsan.wishlist.dto.WishListRequestDTO;
import br.com.claudsan.wishlist.dto.WishListUpdateRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Customer WishList")
public interface CustomerWishlistControllerDocs {

    @Operation(summary = "Find wishlist by customer ID if contains Product ID")
    Flux<WishList> getProductInWishlist(Long customerId, Long productId);

    @Operation(summary = "Find wishlist by ID")
    Mono<WishList> getWishListById(String idWishlist);

    @Operation(summary = "Create wishlist for customer")
    Mono<WishList> createWishlist(WishListRequestDTO wishList);

    @Operation(summary = "Create wishlist for customer")
    Mono<WishList> updateWishListDataById(String idWishlist, WishListUpdateRequestDTO wishList);

    @Operation(summary = "Find all wishlist by customer id")
    Flux<WishList> getWishListByCustomerId(Long idCustomer);

    @Operation(summary = "Delete wishlist by ID")
    Mono<Void> deleteWishlist(String idWishlist);

    @Operation(summary = "Remove product from wishlist")
    Mono<WishList> removeProductInWishlist(String idWishlist, Long productId);

    @Operation(summary = "Add new product in wishlist")
    Mono<WishList> addProduct(String idWishlist, ProductDTO product);

    @Operation(summary = "Find product in wishlist")
    Mono<Product> getProductInWishlist(@PathVariable String wishlistId, @PathVariable Long productId);
}
