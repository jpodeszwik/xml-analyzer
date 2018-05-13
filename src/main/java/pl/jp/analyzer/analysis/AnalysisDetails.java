package pl.jp.analyzer.analysis;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.OffsetDateTime;
import javax.annotation.Nullable;
import org.immutables.value.Value;

@Value.Immutable(singleton = true)
@JsonSerialize(as = ImmutableAnalysisDetails.class)
public interface AnalysisDetails {
    @Nullable
    OffsetDateTime firstPost();

    @Nullable
    OffsetDateTime lastPost();

    @Value.Default
    default long totalPosts() {
        return 0;
    }

    @Nullable
    Long totalAcceptedPosts();

    @Value.Default
    default long avgScore() {
        return 0;
    }
}
