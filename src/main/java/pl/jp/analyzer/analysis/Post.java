package pl.jp.analyzer.analysis;

import java.time.OffsetDateTime;
import javax.annotation.Nullable;
import org.immutables.value.Value;

@Value.Immutable
interface Post {
    int id();

    int score();

    OffsetDateTime creationDate();

    @Nullable
    Integer acceptedAnswerId();
}
