package br.com.claudsan.wishlist.stub;

import br.com.claudsan.wishlist.document.WishList;
import br.com.claudsan.wishlist.dto.WishListRequestDTO;

public interface WishlistStub {

    static final Long CUSTOMER_ID = 1000L;

    static WishListRequestDTO buildRequestDTO(){
        var wish = new WishListRequestDTO();
        wish.setTitle("Wishlist example");
        wish.setCustomerId(CUSTOMER_ID);
        wish.setProducts(ProductStub.buildDTO(1));
        return wish;
    }

    static WishList build(){
        var wish = new WishList();
        wish.setTitle("Wishlist example");
        wish.setCustomerId(CUSTOMER_ID);
        wish.setProducts(ProductStub.build(1));
        return wish;
    }

}
