package pl.kamil.chefscookbook.shared.response;

import lombok.Value;

@Value
public class Response<T>{

    boolean success;
    T data;
    String error;

    public static <T> Response<T> failure(String error) {
        return new Response<>(false, null, error);
    }
    public static <T> Response<T> success(T target) {
        return new Response<>(true, target, null);
    }
}
