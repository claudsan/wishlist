package br.com.claudsan.wishlist.document;

import br.com.claudsan.wishlist.validators.anotation.WishlistMaxProducts;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
