package br.com.claudsan.wishlist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO implements Serializable {
    @Min(value = 1, message = "Product ID is invalid!")
    @NotNull(message = "Id of product is required")
    @Schema(description = "Product ID")
    private Long productId;

    @NotNull(message = "Title of product is required")
    @Schema(description = "Title of product")
    private String title;

    @NotNull(message = "Field url is required")
    @Schema(description = "Product url")
    private String url;
}
