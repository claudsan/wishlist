package br.com.claudsan.wishlist.repository;

import br.com.claudsan.wishlist.document.WishList;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface WishListRepository extends ReactiveMongoRepository<WishList, String> {

    Flux<WishList> findAllByCustomerIdAndProducts_ProductId(Long customerId, Long productId);

    Mono<WishList> findByIdAndProducts_ProductId(String wishlistId, Long productId);
}
