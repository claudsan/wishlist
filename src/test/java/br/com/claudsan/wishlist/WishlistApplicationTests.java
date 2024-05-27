package br.com.claudsan.wishlist;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WishlistApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void applicationContextTest() {
        WishlistApplication.main(new String[] {});
    }
}
