package errors;

/**
 * Wrapper class for returning a JSON object of format:
 * {
 *     error: "message"
 * }
 */
public class JsonError {
    private String error;

    public JsonError(String error) {
        this.error = error;
    }
}
