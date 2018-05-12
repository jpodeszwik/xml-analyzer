package pl.jp.analyzer.analysis;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PostAnalyzerTest {
    private static final OffsetDateTime POST_1_CREATION_DATE = OffsetDateTime.now();
    private static final OffsetDateTime POST_2_CREATION_DATE = POST_1_CREATION_DATE.plus(5, ChronoUnit.MINUTES);

    private static final Post POST_1 = ImmutablePost.builder()
            .id(1).score(10).creationDate(POST_1_CREATION_DATE).build();

    private static final Post POST_2 = ImmutablePost.builder()
            .id(2).score(5).creationDate(POST_2_CREATION_DATE).build();

    @Test
    public void forEmptyPosts_shouldReturnZeroValues() {
        Stream<Post> posts = Stream.empty();

        AnalysisDetails analysisDetails = PostAnalyzer.analyzePosts(posts);

        assertThat(analysisDetails.totalPosts()).isEqualTo(0);
        assertThat(analysisDetails.avgScore()).isEqualTo(0);
        assertThat(analysisDetails.firstPost()).isNull();
        assertThat(analysisDetails.lastPost()).isNull();
    }

    @Test
    public void forSinglePost_shouldReturnItsValues() {
        Stream<Post> posts = Stream.of(POST_1);

        AnalysisDetails analysisDetails = PostAnalyzer.analyzePosts(posts);

        assertThat(analysisDetails.totalPosts()).isEqualTo(1);
        assertThat(analysisDetails.avgScore()).isEqualTo(POST_1.score());
        assertThat(analysisDetails.firstPost()).isEqualTo(POST_1_CREATION_DATE);
        assertThat(analysisDetails.lastPost()).isEqualTo(POST_1_CREATION_DATE);
    }

    @Test
    public void forTwoPosts_shouldCountStatistics() {
        Stream<Post> posts = Stream.of(POST_1, POST_2);

        AnalysisDetails analysisDetails = PostAnalyzer.analyzePosts(posts);

        assertThat(analysisDetails.totalPosts()).isEqualTo(2);
        assertThat(analysisDetails.avgScore()).isEqualTo((POST_1.score() + POST_2.score()) / 2);
        assertThat(analysisDetails.firstPost()).isEqualTo(POST_1_CREATION_DATE);
        assertThat(analysisDetails.lastPost()).isEqualTo(POST_2_CREATION_DATE);
    }
}