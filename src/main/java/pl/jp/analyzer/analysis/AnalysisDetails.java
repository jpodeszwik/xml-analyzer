package pl.jp.analyzer.analysis;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.OffsetDateTime;
import javax.annotation.Nullable;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableAnalysisDetails.class)
public interface AnalysisDetails {
    @Nullable
    OffsetDateTime firstPost();

    @Nullable
    OffsetDateTime lastPost();

    long totalPosts();

    @Nullable
    Long totalAcceptedPosts();

    long avgScore();
}
