package com.moviePicker.api.common.exceptionHandler;

import com.moviePicker.api.auth.exception.*;
import com.moviePicker.api.common.exception.NotSameException;
import com.moviePicker.api.member.exception.DuplicateMemberException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, NotSameException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> BadRequestHandler(Exception e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", e.getMessage());

        checkMethodArgumentNotValidException(e, errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler({UnauthenticatedException.class, WrongPasswordException.class})
    public ResponseEntity<Map<String, String>> unauthorizedHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap("message", e.getMessage()));
    }

    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<Map<String, String>> forbiddenHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap("message", e.getMessage()));
    }

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<Map<String, String>> notFoundHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", e.getMessage()));
    }

    @ExceptionHandler({ExpiredTokenException.class, WrongTokenException.class, DuplicateMemberException.class})
    public ResponseEntity<Map<String, String>> conflictHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap("message", e.getMessage()));
    }

    private void checkMethodArgumentNotValidException(Exception e, Map<String, String> errors) {
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            errors.clear();
            StringBuilder sb = new StringBuilder();
            ex.getBindingResult().getAllErrors()
                    .forEach(c -> sb.append(c.getDefaultMessage()).append(". "));
            errors.put("message", sb.toString());
        }
    }

}
