package com.itonse.tableup.manager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ResponseError {
    private String field;
    private String message;

    public static ResponseError of(FieldError e) {   // 에러 하나에 대해서
        return ResponseError.builder()
                .field(e.getField())
                .message(e.getDefaultMessage())
                .build();
    }

    public ResponseEntity<?> ResponseErrorList(Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<>();

        errors.getAllErrors().stream().forEach((e) -> {
            responseErrorList.add(ResponseError.of((FieldError) e));
        });

        return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
    }
}