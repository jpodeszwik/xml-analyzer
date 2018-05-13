package pl.jp.analyzer.analysis;

public class XmlFileException extends RuntimeException {
    XmlFileException(String message) {
        super(message);
    }

    XmlFileException(String message, Throwable t) {
        super(message, t);
    }
}
