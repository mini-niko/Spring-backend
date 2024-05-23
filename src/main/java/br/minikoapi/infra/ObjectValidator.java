package br.minikoapi.infra;

import br.minikoapi.entities.validator.IValidator;
import br.minikoapi.entities.validator.ValidationDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
public class ObjectValidator {
    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();

    public ValidationDTO validate(IValidator object) {
        Set<ConstraintViolation<IValidator>> constraintViolations = validator.validate(object);
        String message = constraintViolations
                .stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("\n"));

        System.out.println(message);

        return new ValidationDTO(!(message.isEmpty() || message == null), message);
    }
}
