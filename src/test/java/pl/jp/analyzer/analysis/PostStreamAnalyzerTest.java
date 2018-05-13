package pl.jp.analyzer.analysis;

import java.util.stream.Stream;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.jp.analyzer.analysis.TestData.POST_1;
import static pl.jp.analyzer.analysis.TestData.POST_2;

public class PostStreamAnalyzerTest {
    private PostStreamAnalyzer postStreamAnalyzer = new PostStreamAnalyzer();

    @Test
    public void forEmptyPosts_shouldReturnDefaultAnalysisDetails() {
        // given
        Stream<Post> posts = Stream.empty();

        // when
        PostStats postStats = postStreamAnalyzer.analyzePosts(posts);

        // then
        assertThat(postStats).isEqualTo(ImmutablePostStats.of());
    }

    @Test
    public void forSinglePost_shouldReturnItsValues() {
        // given
        Stream<Post> posts = Stream.of(POST_1);

        // when
        PostStats postStats = postStreamAnalyzer.analyzePosts(posts);

        // then
        assertThat(postStats.totalPosts()).isEqualTo(1);
        assertThat(postStats.avgScore()).isEqualTo(POST_1.score());
        assertThat(postStats.firstPost()).isEqualTo(POST_1.creationDate());
        assertThat(postStats.lastPost()).isEqualTo(POST_1.creationDate());
    }

    @Test
    public void forTwoPosts_shouldCountStatistics() {
        // given
        Stream<Post> posts = Stream.of(POST_1, POST_2);

        // when
        PostStats postStats = postStreamAnalyzer.analyzePosts(posts);

        // then
        assertThat(postStats.totalPosts()).isEqualTo(2);
        assertThat(postStats.avgScore()).isEqualTo((POST_1.score() + POST_2.score()) / 2);
        assertThat(postStats.firstPost()).isEqualTo(POST_1.creationDate());
        assertThat(postStats.lastPost()).isEqualTo(POST_2.creationDate());
    }
}