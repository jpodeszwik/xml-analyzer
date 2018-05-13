package pl.jp.analyzer.api.analyze;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.OffsetDateTime;
import org.immutables.value.Value;
import pl.jp.analyzer.analysis.PostStats;

@Value.Immutable
@JsonSerialize(as = ImmutableAnalyzeResponse.class)
public interface AnalyzeResponse {
    OffsetDateTime analyseDate();

    PostStats details();
}
