package pl.kamil.chefscookbook.shared.response;

import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Value
public class Response<T>{

    boolean success;
    T data;
    String error;


    public static <T> Response<T> failure(String error) {
        return new Response<>(false, null, error);
    }
    public static <T> Response<T> success(T data) {
        return new Response<>(true, data, null);
    }
}
