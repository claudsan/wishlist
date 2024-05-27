package br.com.claudsan.wishlist.service.impl;

import br.com.claudsan.wishlist.document.Product;
import br.com.claudsan.wishlist.document.WishList;
import br.com.claudsan.wishlist.exceptions.CreateErrorException;
import br.com.claudsan.wishlist.exceptions.NotFoundException;
import br.com.claudsan.wishlist.exceptions.QtdeProductsReachedException;
import br.com.claudsan.wishlist.metrics.CustomMetrics;
import br.com.claudsan.wishlist.repository.WishListRepository;
import br.com.claudsan.wishlist.service.WishListService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static br.com.claudsan.wishlist.metrics.CustomMetrics.WISHLIST_ADD_PRODUCTS;
import static br.com.claudsan.wishlist.metrics.CustomMetrics.WISHLIST_CREATED;
import static java.lang.String.format;

@Service
@Log4j2
public class WishListServiceImpl implements WishListService {

    public WishListServiceImpl(@Value("${wishlist.max.products}") Integer maxQtdProducts, CustomMetrics metrics, WishListRepository repository) {
        this.maxQtdProducts = maxQtdProducts;
        this.metrics = metrics;
        this.repository = repository;
    }

    private final Integer maxQtdProducts;
    private final CustomMetrics metrics;
    private final WishListRepository repository;

    @Override
    public Mono<WishList> getWishListById(String idWishlist) {
        return repository.findById(idWishlist)
                .switchIfEmpty(Mono.error(new NotFoundException("Wishlist not exists for id: "+idWishlist)));
    }

    @Override
    public Flux<WishList> getWishListByCustomer(Long customerId) {
        return repository.findAll(Example.of(WishList.builder()
                .customerId(customerId)
                .build())
                ).switchIfEmpty(Mono.error(new NotFoundException(format("Ccustomer %s does not have any wishlist", customerId))));
    }

    @Override
    public Flux<WishList> getWishListByCustomerAndProductId(Long customerId, Long productId) {
        return repository.findAllByCustomerIdAndProducts_ProductId(customerId, productId)
                .switchIfEmpty(Mono.error(new NotFoundException(format("Product not exists in wishlist customer %d product %d", customerId, productId))));
    }

    @Override
    public Mono<WishList> createWishList(WishList wishList) {
        wishList.setId(getNewId());
        return repository.save(wishList)
                .switchIfEmpty(Mono.error(new CreateErrorException("Error on create new Wishlist for Customer:"+wishList.getCustomerId())))
                .doOnSuccess(w -> {
                    metrics.counter(WISHLIST_CREATED);
                    w.getProducts().iterator().forEachRemaining(p->metrics.counter(WISHLIST_ADD_PRODUCTS));
                    log.info("Wish list created {} for customer {}", w.getId(), w.getCustomerId());
                });
    }

    @Override
    public Mono<Void> deleteWishList(String idWishlist) {
        return getWishListById(idWishlist)
                .map(wishList -> {
                    log.info("Removed Wishlist id {}", idWishlist);
                    return wishList;
                })
                .flatMap(repository::delete);
    }

    @Override
    public Mono<WishList> removeListProductInWishlist(String idWishlist, Long productId) {
        return getWishListById(idWishlist)
                .map(wishList -> {
                    wishList.getProducts().removeIf(product -> product.getProductId().equals(productId));
                    log.info("Product id {} removed from Wishlist id {}", productId, idWishlist);
                    return wishList;
                })
                .flatMap(repository::save);
    }

    @Override
    public Mono<WishList> addProductInWishList(String idWishlist, Product product) {
        return getWishListById(idWishlist)
                .<WishList>handle((wishList, sink) -> {
                    wishList.getProducts().add(product);
                    if(wishList.getProducts().size() > maxQtdProducts) {
                        sink.error(new QtdeProductsReachedException());
                        return;
                    }
                    metrics.counter(WISHLIST_ADD_PRODUCTS);
                    log.info("Product id {} add to Wishlist id {}",product.getProductId(), idWishlist);
                    sink.next(wishList);
                })
                .flatMap(repository::save);
    }

    @Override
    public Mono<WishList> updateWishListData(WishList wishListData) {
        return getWishListById(wishListData.getId())
                .map(wishList -> {
                    wishListData.setProducts(wishList.getProducts());
                    wishListData.setCustomerId(wishList.getCustomerId());
                    log.info("Update wishlist data id {} ",wishListData.getId());
                    return wishListData;
                })
                .flatMap(repository::save);
    }

    @Override
    public Mono<Product> findProductInWishlist(String wishlistId, Long productId) {
        return repository.findByIdAndProducts_ProductId(wishlistId, productId)
                .switchIfEmpty(
                        Mono.error(new NotFoundException(
                                format("Product not exists for Wishlist id: %s and product id %d",wishlistId, productId))
                        )
                ).map(wishList -> wishList.getProducts().stream()
                        .filter(product -> product.getProductId().equals(productId)).toList())
                .map(products -> products.get(0));
    }

    private String getNewId(){
        return UUID.randomUUID().toString();
    }
}
