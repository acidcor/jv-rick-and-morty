package mate.academy.rickandmorty.exception;

public class EmptyDataFromApiException extends RuntimeException {
    public EmptyDataFromApiException(String message) {
        super(message);
    }
}
