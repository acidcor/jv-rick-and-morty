package mate.academy.rickandmorty.exception;

public class FetchingDataFromApiException extends RuntimeException {
    public FetchingDataFromApiException(String message, Exception e) {
        super(message, e);
    }

    public FetchingDataFromApiException(String message) {
        super(message);
    }
}
