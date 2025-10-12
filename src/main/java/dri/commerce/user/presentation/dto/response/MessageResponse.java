package dri.commerce.user.presentation.dto.response;

public record MessageResponse(
        String message,
        boolean success
) {

    public static MessageResponse success(String message) {
        return new MessageResponse(message, true);
    }

    public static MessageResponse error(String message) {
        return new MessageResponse(message, false);
    }
}
