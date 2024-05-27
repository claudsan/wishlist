package br.com.claudsan.wishlist.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class WishListUpdateRequestDTO {

    @NotEmpty(message = "Title is required")
    @Schema(description = "Title of Wishlist")
    private String title;

}
