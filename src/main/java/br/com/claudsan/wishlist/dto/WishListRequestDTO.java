package br.com.claudsan.wishlist.dto;


import br.com.claudsan.wishlist.validators.anotation.WishlistMaxProducts;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class WishListRequestDTO implements Serializable {

    @NotEmpty(message = "Title is required")
    @Schema(description = "Title of Wishlist")
    private String title;

    @Min(value = 1, message = "Customer ID is invalid!")
    @NotNull(message = "Customer ID is required")
    @Schema(description = "Customer ID")
    private Long customerId;

    @Valid
    @WishlistMaxProducts
    @Schema(description = "List of produts")
    private Set<ProductDTO> products;

}
