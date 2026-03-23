package tn.esprit.disasteraidbackend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter

public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    public ApiResponse() {
    }

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

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<T>(false, message, null);
    }
}