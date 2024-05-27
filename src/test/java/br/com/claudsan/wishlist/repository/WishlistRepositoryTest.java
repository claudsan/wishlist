package br.com.claudsan.wishlist.repository;

import br.com.claudsan.wishlist.document.WishList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import java.util.UUID;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WishlistRepositoryTest {


    @Autowired
    private WishListRepository repository;

    @Test
    @DisplayName(value = "Test create new wishlist")
    void create(){
        var id = getId();
        var title = "new wishlist";
        var newWish = repository.save(WishList.builder().id(id).title(title).build()).block();

        Assertions.assertEquals(repository.count(Example.of(WishList.builder().id(id).build())).block(),1);
        Assertions.assertNotNull(newWish);
        Assertions.assertNotNull(newWish.getId());
        Assertions.assertEquals(newWish.getId(), id);
        Assertions.assertEquals(newWish.getTitle(), title);

        clear();
    }

    @Test
    @DisplayName(value = "Test find all wishlists")
    void findAll(){
        var title = "new wishlist";
        repository.save(WishList.builder().id(getId()).title(title).build()).block().getId();
        repository.save(WishList.builder().id(getId()).title(title).build()).block().getId();
        repository.save(WishList.builder().id(getId()).title(title).build()).block().getId();

        Assertions.assertEquals(repository.count().block(),3);

        clear();

    }

    @Test
    @DisplayName(value = "Test find all wishlists by customer id")
    void findAllByCustomer(){
        var title = "new wishlist";
        repository.save(WishList.builder().id(getId()).customerId(100L).title(title).build()).block().getId();
        repository.save(WishList.builder().id(getId()).customerId(100L).title(title).build()).block().getId();
        repository.save(WishList.builder().id(getId()).customerId(888L).title(title).build()).block().getId();

        Assertions.assertEquals(repository.count().block(),3);
        var whises = repository.findAll(Example.of(WishList.builder().customerId(100L).build()));
        Assertions.assertEquals(whises.buffer().blockFirst().size(),2);

        clear();

    }

    void clear(){
        repository.deleteAll().block();
        Assertions.assertEquals(repository.count().block(),0);
    }

    String getId(){
        return UUID.randomUUID().toString();
    }
}
