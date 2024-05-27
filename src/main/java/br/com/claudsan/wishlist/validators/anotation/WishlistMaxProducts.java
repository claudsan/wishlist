package br.com.claudsan.wishlist.validators.anotation;

import br.com.claudsan.wishlist.validators.impl.WishlistMaxProductsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = { WishlistMaxProductsValidator.class })
@Documented
public @interface WishlistMaxProducts {

    String message() default "Limit of products is reached.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

