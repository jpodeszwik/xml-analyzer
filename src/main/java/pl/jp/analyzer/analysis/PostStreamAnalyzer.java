package pl.jp.analyzer.analysis;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
class PostStreamAnalyzer {
    private static final Collector<Post, PartialResult, PostStats> POST_STATISTICS_COLLECTOR = Collector.of(
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
        private int acceptedPosts = 0;

        private PartialResult addPost(Post post) {
            count += 1;
            scoreSum += post.score();
            if (lastPost == null || lastPost.isBefore(post.creationDate())) {
                lastPost = post.creationDate();
            }
            if (firstPost == null || firstPost.isAfter(post.creationDate())) {
                firstPost = post.creationDate();
            }

            if (Objects.nonNull(post.acceptedAnswerId())) {
                acceptedPosts += 1;
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
            acceptedPosts += other.acceptedPosts;
            return this;
        }
    }

    private static PostStats finish(PartialResult partialResult) {
        return ImmutablePostStats.builder()
                .totalPosts(partialResult.count)
                .avgScore(partialResult.count == 0 ? 0 : partialResult.scoreSum / partialResult.count)
                .firstPost(partialResult.firstPost)
                .lastPost(partialResult.lastPost)
                .totalAcceptedPosts(partialResult.acceptedPosts)
                .build();
    }

    PostStats analyzePosts(Stream<Post> posts) {
        return posts
                .collect(POST_STATISTICS_COLLECTOR);
    }
}
