package pl.jp.api.analyze;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.OffsetDateTime;
import javax.annotation.Nullable;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableAnalyzeDetails.class)
public interface AnalyzeDetails {
    @Nullable
    OffsetDateTime firstPost();

    @Nullable
    OffsetDateTime lastPost();

    long totalPosts();

    @Nullable
    Long totalAcceptedPosts();

    long avgScore();
}
