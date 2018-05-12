package pl.jp.analyzer.api.index;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableIndexResponse.class)
public interface IndexResponse {
    String applicationName();
}
