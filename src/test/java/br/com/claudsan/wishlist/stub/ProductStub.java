package br.com.claudsan.wishlist.stub;

import br.com.claudsan.wishlist.document.Product;
import br.com.claudsan.wishlist.dto.ProductDTO;

import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;

public interface ProductStub {

    static Set<ProductDTO> buildDTO(int qtd){
        Set<ProductDTO> set = new HashSet<>();
        for (int i = 0; i <qtd; i++) {
            var p = new ProductDTO();
            p.setProductId(100L+i);
            p.setTitle(format("Example product %d", i+1));
            p.setUrl(format("https://loja.com/product/id/%d", i+1));
            set.add(p);
        }
        return set;
    }

    static Set<Product> build(int qtd) {
        Set<Product> set = new HashSet<>();
        for (int i = 0; i <qtd; i++) {
            var p = new Product();
            p.setProductId(100L+i);
            p.setTitle(format("Example product %d", i+1));
            p.setUrl(format("https://loja.com/product/id/%d", i+1));
            set.add(p);
        }
        return set;
    }
}
