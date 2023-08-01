package com.guterres.adriano.libraryapi.api.exceptions;

import com.guterres.adriano.libraryapi.exception.BusinessException;
import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiErrors {

    @Getter
    private List <String> errors;

    public ApiErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
    }
    public ApiErrors(BusinessException exception) {
        this.errors = Arrays.asList(exception.getMessage());

    }
}
