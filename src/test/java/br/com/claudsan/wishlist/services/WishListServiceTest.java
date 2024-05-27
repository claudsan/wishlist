package br.com.claudsan.wishlist.services;

import br.com.claudsan.wishlist.document.Product;
import br.com.claudsan.wishlist.document.WishList;
import br.com.claudsan.wishlist.repository.WishListRepository;
import br.com.claudsan.wishlist.service.impl.WishListServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;
import java.util.UUID;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WishListServiceTest {

    @Autowired
    private WishListRepository repository;

    @Autowired
    private WishListServiceImpl service;

    @Test
    @DisplayName(value = "Find whislist by Id")
    void getWishListById() {
        var id =repository.save(WishList.builder().id(UUID.randomUUID().toString()).build()).block().getId();
        var wish = service.getWishListById(id).block();

        Assertions.assertNotNull(wish);
        Assertions.assertEquals(id, wish.getId());

        clear();
    }

    @Test
    @DisplayName(value = "Find whislist by customerId")
    void getWishListByCustomer() {
        var idCustomerOne = 100L;
        var idCustomerTwo = 101L;
        repository.save(WishList.builder().id(UUID.randomUUID().toString()).customerId(idCustomerOne).build()).block();
        repository.save(WishList.builder().id(UUID.randomUUID().toString()).customerId(idCustomerOne).build()).block();
        repository.save(WishList.builder().id(UUID.randomUUID().toString()).customerId(idCustomerTwo).build()).block();

        var wishListTOne = service.getWishListByCustomer(idCustomerOne).collectList().block();
        var wishListTwo = service.getWishListByCustomer(idCustomerTwo).collectList().block();

        Assertions.assertNotNull(wishListTOne);
        Assertions.assertEquals(wishListTOne.size(), 2);

        Assertions.assertNotNull(wishListTwo);
        Assertions.assertEquals(wishListTwo.size(), 1);

        clear();

    }

    @Test
    @DisplayName(value = "Find product in whislist by customerId and productId")
    void getWishListByCustomerAndProductId() {
        var customerId = 1L;
        var productId = 2L;
        var wish = service.createWishList(WishList.builder()
                .customerId(customerId).products(Set.of(Product.builder().productId(productId).build()))
                .title("New WishList")
                .build()).block();

        Assertions.assertNotNull(wish);
        var result = service.getWishListByCustomerAndProductId(wish.getCustomerId(), wish.getProducts().iterator().next().getProductId()).blockFirst();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getCustomerId(), customerId);

        clear();
    }

    @DisplayName(value = "Create new whislist")
    @Test
    void createWishList() {
        var wish = service.createWishList(WishList.builder()
                .customerId(1L)
                .title("New WishList")
                .build()).block();

        Assertions.assertNotNull(wish);
        Assertions.assertNotNull(wish.getId());

        clear();
    }

    @Test
    @DisplayName(value = "Update wishlist data")
    void  updateWishListData(){
        var newTitle = "Update wishlist";
        var wish = service.createWishList(WishList.builder()
                .customerId(1L)
                .title("New WishList")
                .customerId(1L)
                .products(Set.of(
                        Product.builder().productId(1L).build(),
                        Product.builder().productId(2L).build()
                ))
                .build()).block();

        Assertions.assertNotNull(wish);
        var newWishData = WishList.builder().id(wish.getId())
                .customerId(wish.getCustomerId())
                .title(newTitle).build();

        newWishData = service.updateWishListData(newWishData).block();
        Assertions.assertNotNull(newWishData);
        Assertions.assertEquals(newWishData.getTitle(), newTitle);
        Assertions.assertEquals(newWishData.getProducts().size(), wish.getProducts().size());

        clear();
    }

    @Test
    @DisplayName(value = "Delete whislist by id")
    void deleteWishList() {
        var idWish = repository.save(WishList.builder().id(UUID.randomUUID().toString()).build()).block().getId();
        service.deleteWishList(idWish).block();

        Assertions.assertEquals(repository.count().block(), 0);
    }

    @Test
    @DisplayName(value = "Delete product from whislist by idWishlist and productId")
    void deleteProductInWishlist() {
        var wish = service.createWishList(WishList.builder()
                .customerId(1L)
                .title("New WishList")
                .customerId(1L)
                .products(Set.of(
                        Product.builder().productId(1L).build(),
                        Product.builder().productId(2L).build()
                ))
                .build()).block();

        Assertions.assertNotNull(wish);
        final int oldQtdProducts = wish.getProducts().size();
        wish = service.removeListProductInWishlist(wish.getId(), 1L).block();

        Assertions.assertNotNull(wish);
        Assertions.assertNotEquals(wish.getProducts().size(), oldQtdProducts);
        Assertions.assertEquals(wish.getProducts().size(), 1);

        clear();
    }

    @Test
    @DisplayName(value = "Add new product to whislist")
    void addProductInWishList() {
        var wish = service.createWishList(WishList.builder()
                .customerId(1L)
                .title("New WishList")
                .customerId(1L)
                .products(Set.of(
                        Product.builder().productId(1L).build(),
                        Product.builder().productId(2L).build()
                ))
                .build()).block();

        Product product = Product.builder()
                .productId(3L).title("New product added").url("https://google.com")
                .build();

        Assertions.assertNotNull(wish);
        final int oldQtdProducts = wish.getProducts().size();
        wish = service.addProductInWishList(wish.getId(), product).block();

        Assertions.assertNotNull(wish);
        Assertions.assertNotEquals(wish.getProducts().size(), oldQtdProducts);
        Assertions.assertEquals(wish.getProducts().size(), 3);

        clear();
    }

    void clear(){
        repository.deleteAll().block();
        Assertions.assertEquals(repository.count().block(),0);
    }
}
