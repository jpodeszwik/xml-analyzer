package pl.jp.api.analyze;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableAnalyzeRequest.class)
public interface AnalyzeRequest {
    String url();
}
