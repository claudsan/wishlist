package br.com.claudsan.wishlist.validators.impl;

import br.com.claudsan.wishlist.document.Product;
import br.com.claudsan.wishlist.dto.ProductDTO;
import br.com.claudsan.wishlist.validators.anotation.WishlistMaxProducts;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

public class WishlistMaxProductsValidator implements ConstraintValidator<WishlistMaxProducts, Set<ProductDTO>> {

    @Value("${wishlist.max.products}")
    private Long qtdMax;

    @Override
    public void initialize(WishlistMaxProducts constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Set<ProductDTO> products, ConstraintValidatorContext constraintValidatorContext) {
        if(products != null)
            return products.size() <= qtdMax;

        return true;
    }


}
