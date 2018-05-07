package pl.jp.api.analyze;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.OffsetDateTime;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableAnalyseDetails.class)
public interface AnalyseDetails {
    OffsetDateTime firstPost();

    OffsetDateTime lastPost();

    long totalPosts();

    long totalAcceptedPosts();

    long avgScore();
}
