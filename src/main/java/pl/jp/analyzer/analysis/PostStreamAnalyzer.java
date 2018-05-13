package pl.jp.analyzer.analysis;

import java.time.OffsetDateTime;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
class PostStreamAnalyzer {
    private static final Collector<Post, PartialResult, AnalysisDetails> POST_STATISTICS_COLLECTOR = Collector.of(
            PartialResult::new,
            PartialResult::addPost,
            PartialResult::combine,
            PostStreamAnalyzer::finish
    );

    private static class PartialResult {
        private int count = 0;
        private long scoreSum = 0;
        private OffsetDateTime lastPost;
        private OffsetDateTime firstPost;

        private PartialResult addPost(Post post) {
            count += 1;
            scoreSum += post.score();
            if (lastPost == null || lastPost.isBefore(post.creationDate())) {
                lastPost = post.creationDate();
            }
            if (firstPost == null || firstPost.isAfter(post.creationDate())) {
                firstPost = post.creationDate();
            }

            return this;
        }

        private PartialResult combine(PartialResult other) {
            count += other.count;
            scoreSum += other.scoreSum;
            if (lastPost.isBefore(other.lastPost)) {
                lastPost = other.lastPost;
            }
            if (firstPost.isAfter(other.firstPost)) {
                firstPost = other.firstPost;
            }
            return this;
        }
    }

    private static AnalysisDetails finish(PartialResult partialResult) {
        return ImmutableAnalysisDetails.builder()
                .totalPosts(partialResult.count)
                .avgScore(partialResult.count == 0 ? 0 : partialResult.scoreSum / partialResult.count)
                .firstPost(partialResult.firstPost)
                .lastPost(partialResult.lastPost)
                .build();
    }

    AnalysisDetails analyzePosts(Stream<Post> posts) {
        return posts
                .collect(POST_STATISTICS_COLLECTOR);
    }
}
