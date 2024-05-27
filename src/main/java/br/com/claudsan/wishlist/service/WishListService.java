package br.com.claudsan.wishlist.service;

import br.com.claudsan.wishlist.document.Product;
import br.com.claudsan.wishlist.document.WishList;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WishListService {

    Mono<WishList> getWishListById(String wishlistId);
    Flux<WishList> getWishListByCustomer(Long customerId);
    Flux<WishList> getWishListByCustomerAndProductId(Long customerId, Long productId);
    Mono<WishList> createWishList(WishList wishList);
    Mono<Void> deleteWishList(String wishlistId);
    Mono<WishList> removeListProductInWishlist(String wishlistId, Long productId);
    Mono<WishList> addProductInWishList(String wishlistId, Product productId);
    Mono<WishList> updateWishListData(WishList title);
    Mono<Product> findProductInWishlist(String wishlistId, Long productId);

}
