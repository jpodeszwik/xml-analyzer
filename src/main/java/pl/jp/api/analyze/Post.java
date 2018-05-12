package pl.jp.api.analyze;

import org.immutables.value.Value;

@Value.Immutable
public interface Post {
    int id();

    int score();
}
