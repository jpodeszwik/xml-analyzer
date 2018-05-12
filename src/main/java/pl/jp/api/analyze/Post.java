package pl.jp.api.analyze;

import java.time.OffsetDateTime;
import org.immutables.value.Value;

@Value.Immutable
public interface Post {
    int id();

    int score();

    OffsetDateTime creationDate();
}
