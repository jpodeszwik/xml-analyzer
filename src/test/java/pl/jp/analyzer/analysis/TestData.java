package pl.jp.analyzer.analysis;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

class TestData {
    static final Post POST_1 = ImmutablePost.builder()
            .id(1).score(10).creationDate(LocalDateTime.parse("2016-01-01T06:00:00.000").atOffset(ZoneOffset.UTC)).build();

    static final Post POST_2 = ImmutablePost.builder()
            .id(2).score(5).creationDate(LocalDateTime.parse("2016-01-01T08:00:00.000").atOffset(ZoneOffset.UTC)).build();
}
