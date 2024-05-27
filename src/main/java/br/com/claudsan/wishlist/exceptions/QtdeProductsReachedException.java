package br.com.claudsan.wishlist.exceptions;

public class QtdeProductsReachedException extends RuntimeException{

    public QtdeProductsReachedException() {
        super("Wishlist reached limite of products");
    }
}
