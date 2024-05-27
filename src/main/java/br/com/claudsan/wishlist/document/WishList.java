package br.com.claudsan.wishlist.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class WishList {

    @Id
    private String id;

    private String title;

    @Indexed
    private Long customerId;

    private Set<Product> products;

    public Set<Product> getProducts() {
        return products == null ? new HashSet<>() : products;
    }
}
