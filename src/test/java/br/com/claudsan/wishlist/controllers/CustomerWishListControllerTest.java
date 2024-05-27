package br.com.claudsan.wishlist.controllers;


import br.com.claudsan.wishlist.document.Product;
import br.com.claudsan.wishlist.document.WishList;
import br.com.claudsan.wishlist.dto.ProductDTO;
import br.com.claudsan.wishlist.dto.WishListUpdateRequestDTO;
import br.com.claudsan.wishlist.repository.WishListRepository;
import br.com.claudsan.wishlist.stub.ProductStub;
import br.com.claudsan.wishlist.stub.WishlistStub;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Objects;

import static br.com.claudsan.wishlist.stub.WishlistStub.CUSTOMER_ID;

@SpringBootTest
@AutoConfigureWebTestClient(timeout = "36000")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerWishListControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private WishListRepository repository;

    private final static String BASE_URL = "/wishlist";

    @Test
    @DisplayName("Create new wishlist with corret post data")
    void createWishlist(){
        webClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(WishlistStub.buildRequestDTO()))
                .exchange()
                .expectStatus().isCreated()
                .returnResult(WishList.class);

        clear();
    }

    @Test
    @DisplayName("Create new wishlist with empty products")
    void createWishlistEmptyProducts(){
        var wish = WishlistStub.buildRequestDTO();
        wish.setProducts(null);
        webClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(wish))
                .exchange()
                .expectStatus().isCreated()
                .returnResult(WishList.class);

        clear();
    }

    @Test
    @DisplayName("Try create new wishlist with many products")
    void createWishlistReachedProductQtd(){
        var wish = WishlistStub.buildRequestDTO();
        wish.setProducts(ProductStub.buildDTO(30));
        webClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(wish))
                .exchange()
                .expectStatus().isBadRequest()
                .returnResult(WishList.class);

        clear();
    }

    @Test
    @DisplayName("Delete wishlist by ID")
    void deleteWishlist(){
        var wish = createNewWishlist();

        webClient.delete()
                .uri(BASE_URL.concat("/").concat(wish.getId()))
                .exchange()
                .expectStatus().isNoContent();
        repository.delete(wish).block();
    }


    @Test
    @DisplayName("Delete wishlist by ID non-existent")
    void tryDeleteWishlistNotFound(){
        webClient.delete()
                .uri(BASE_URL.concat("/9999"))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Try create new wishlist with INCORRECT post data")
    void tryCreateWishlistBadRequest(){
        var invalidWish = WishlistStub.buildRequestDTO();
        invalidWish.setCustomerId(null);
        webClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(invalidWish))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Find wishlist by id")
    void getWishListById(){
        var wish = createNewWishlist();

        var result = webClient
                .get().uri(BASE_URL.concat("/").concat(wish.getId()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(WishList.class);

        var findWish = Objects.requireNonNull(result.returnResult().getResponseBody());
        Assertions.assertEquals(findWish.getCustomerId(), wish.getCustomerId());
        Assertions.assertEquals(findWish.getTitle(), wish.getTitle());
        Assertions.assertEquals(findWish.getProducts().size(), wish.getProducts().size());

        clear();
    }

    @Test
    @DisplayName("Error on find wishlist by id non-existent")
    void tryGetWishListById(){
        webClient.get().uri(BASE_URL.concat("/9999"))
                .exchange().expectStatus()
                .isNotFound();
    }

    @Test
    @DisplayName("Find wishlist by customer")
    void getWishListByCustomerId(){
        repository.save(WishlistStub.build()).block();
        repository.save(WishlistStub.build()).block();

        var result = webClient
                .get().uri(BASE_URL.concat("/customer/".concat(CUSTOMER_ID.toString())))
                .exchange().expectStatus()
                .isOk()
                .returnResult(WishList.class);

        var findWishes = Objects.requireNonNull(result.getResponseBody()).collectList().block();
        Assertions.assertNotNull(findWishes);
        Assertions.assertEquals(findWishes.size(), 2);
        Assertions.assertEquals(findWishes.get(0).getCustomerId(), CUSTOMER_ID);
        Assertions.assertEquals(findWishes.get(1).getCustomerId(), CUSTOMER_ID);

        clear();
    }

    @Test
    @DisplayName("Error on find wishlist by customer id non-existent")
    void tryGetWishListByCustomerId(){
        var result = webClient
                .get().uri(BASE_URL.concat("/customer/9999"))
                .exchange()
                .expectStatus()
                .isNotFound();
    }


    @Test
    @DisplayName("Update wishlist data (change original title)")
    void updateWishlistAccepted(){
        var wish = createNewWishlist();
        var newWishData = new WishListUpdateRequestDTO();
        newWishData.setTitle("Title Updated");

        var result = webClient.put()
                .uri(BASE_URL.concat("/").concat(wish.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(newWishData))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(WishList.class);

        var updateWish = result.returnResult().getResponseBody();
        Assertions.assertNotNull(updateWish);
        Assertions.assertEquals(updateWish.getId(), wish.getId());
        Assertions.assertEquals(updateWish.getCustomerId(), wish.getCustomerId());
        Assertions.assertEquals(updateWish.getProducts().size(), wish.getProducts().size());
        Assertions.assertEquals(updateWish.getTitle(), newWishData.getTitle());
        Assertions.assertNotEquals(updateWish.getTitle(), wish.getTitle());

        clear();
    }

    @Test
    @DisplayName("Try create new wishlist with INCORRECT post data")
    void tryUpdateWishlistBadRequest(){
        webClient.put()
                .uri(BASE_URL.concat("/1000")) //ID Fake
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new WishListUpdateRequestDTO()))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Try update title wishlist with id non-existent")
    void tryUpdateWishlistNotFound(){
        var data = new WishListUpdateRequestDTO();
        data.setTitle("new title");
        webClient.put()
                .uri(BASE_URL.concat("/9999"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(data))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Add new product in wishlist")
    void addProductAccepted(){
        var wish = createNewWishlist();
        Assertions.assertEquals(wish.getProducts().size(), 1);

        var newProduct = new ProductDTO();
        newProduct.setProductId(2L);
        newProduct.setTitle("New Added");
        newProduct.setUrl("https://prod-fake-added.com");

        var result = webClient.put()
                .uri(BASE_URL.concat("/").concat(wish.getId()).concat("/product"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(newProduct))
                .exchange()
                .expectStatus().isOk()
                .expectBody(WishList.class).returnResult().getResponseBody();

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getProducts().size() > 1);

        clear();
    }


    @Test
    @DisplayName("Try to add new product in wishlist non-existent")
    void addProductNotFound(){
        webClient.put()
                .uri(BASE_URL.concat("/9999/product"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ProductStub.build(1).iterator().next()))
                .exchange()
                .expectStatus().isNotFound();
    }


    @Test
    @DisplayName("Try to add new product in wishlist with product contains invalid data")
    void addProductBadRequest(){
        var newProduct = new ProductDTO();
        newProduct.setTitle("New Added");

        webClient.put()
                .uri(BASE_URL.concat("/9999/product"))//Fake id Product
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(newProduct))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Add new product in wishlist with max products qtd")
    void tryToaddProductReached(){
        var wish = WishlistStub.build();
        wish.setProducts(ProductStub.build(20));
        wish = repository.save(wish).block();
        Assertions.assertNotNull(wish);
        Assertions.assertEquals(wish.getProducts().size(), 20);

        var newProduct = new ProductDTO();
        newProduct.setProductId(286532L);
        newProduct.setTitle("New Added");
        newProduct.setUrl("https://prod-fake-added.com");

        webClient.put()
                .uri(BASE_URL.concat("/").concat(wish.getId()).concat("/product"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(newProduct))
                .exchange()
                .expectStatus().is4xxClientError();

        clear();
    }

    @Test
    @DisplayName("Get product in wishlist by ")
    void getProductInWishList(){
        var wish = createNewWishlist();
        Assertions.assertEquals(wish.getProducts().size(), 1);

        var idProduct = wish.getProducts().iterator().next().getProductId();

        var result = webClient.get()
                .uri(BASE_URL.concat("/customer/").concat(CUSTOMER_ID.toString()).concat("/product/").concat(idProduct.toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(WishList[].class).returnResult().getResponseBody();

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.length > 0);

        Assertions.assertEquals(result[0].getProducts().iterator().next().getProductId(), idProduct);

        clear();
    }

    @Test
    @DisplayName("Find product in wishlist")
    void getProductInWishlist(){
        var wish = createNewWishlist();
        Assertions.assertEquals(wish.getProducts().size(), 1);

        var idProduct = wish.getProducts().iterator().next().getProductId();
        var result = webClient.get()
                .uri(BASE_URL.concat("/").concat(wish.getId()).concat("/product/").concat(idProduct.toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class).returnResult().getResponseBody();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getProductId(), idProduct);

        clear();
    }

    @Test
    @DisplayName("Try find product in wishlist non-existent")
    void tryProductInWishlistNotFound(){
        var wish = createNewWishlist();
        Assertions.assertEquals(wish.getProducts().size(), 1);

        var idProduct = wish.getProducts().iterator().next().getProductId();
        var result = webClient.get()
                .uri(BASE_URL.concat("/").concat("9999").concat("/product/").concat(idProduct.toString()))
                .exchange()
                .expectStatus().isNotFound();
        clear();
    }

    @Test
    @DisplayName("Try find product non-existent in wishlist")
    void tryProductNotFoundInWishlist(){
        var wish = createNewWishlist();
        Assertions.assertEquals(wish.getProducts().size(), 1);

        var idProduct = wish.getProducts().iterator().next().getProductId();
        var result = webClient.get()
                .uri(BASE_URL.concat("/").concat(wish.getId()).concat("/product/").concat("9999"))
                .exchange()
                .expectStatus().isNotFound();
        clear();
    }

    @Test
    @DisplayName("Removed product in wishlist")
    void removeProductInWishlist(){
        var wish = WishlistStub.build();
        wish.setProducts(ProductStub.build(3));
        repository.save(wish).block();
        Assertions.assertEquals(wish.getProducts().size(), 3);

        var idFirstProduct = wish.getProducts().iterator().next().getProductId();

        var result = webClient.patch()
                .uri(BASE_URL.concat("/").concat(wish.getId()).concat("/product/").concat(idFirstProduct.toString()))
                .exchange()
                .expectStatus().isAccepted();

        var updateWish = repository.findById(wish.getId()).block();
        Assertions.assertNotNull(updateWish);
        Assertions.assertEquals(2, updateWish.getProducts().size());
        clear();
    }


    @Test
    @DisplayName("Try delete product non-existent in wishlist")
    void tryRemoveProductInWishlist(){
        var wish = createNewWishlist();
        Assertions.assertEquals(wish.getProducts().size(), 1);

        var idProduct = wish.getProducts().iterator().next().getProductId();
        var result = webClient.get()
                .uri(BASE_URL.concat("/").concat(wish.getId()).concat("/product/").concat("9999"))
                .exchange()
                .expectStatus().isNotFound();
        clear();
    }

    WishList createNewWishlist(){
        var wish = repository.save(WishlistStub.build()).block();
        Assertions.assertNotNull(wish);
        return wish;
    }

    void clear(){
        repository.deleteAll().block();
        Assertions.assertEquals(repository.count().block(),0);
    }
}
