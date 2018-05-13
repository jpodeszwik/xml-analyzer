package pl.jp.analyzer.api.analyze;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.net.URL;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableAnalyzeRequest.class)
public interface AnalyzeRequest {
    URL url();

    static AnalyzeRequest of(URL url) {
        return ImmutableAnalyzeRequest.builder().url(url).build();
    }
}
