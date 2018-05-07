package pl.jp.api.analyze;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.OffsetDateTime;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableAnalyzeResponse.class)
public interface AnalyzeResponse {
    OffsetDateTime analyseDate();

    AnalyseDetails details();
}
