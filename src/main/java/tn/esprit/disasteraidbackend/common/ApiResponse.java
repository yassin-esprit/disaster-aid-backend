package tn.esprit.disasteraidbackend.common;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<T>(true, "OK", data);
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<T>(true, message, data);
    }

    public static ApiResponse<Void> error(String message) {
        return new ApiResponse<Void>(false, message, null);
    }
}
