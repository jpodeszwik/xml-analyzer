package pl.jp.analyzer.analysis;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collector;
import java.util.stream.Stream;
import javax.xml.namespace.QName;
import javax.xml.stream.events.StartElement;

class PostAnalyzer {
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

        private AnalysisDetails finish() {
            return ImmutableAnalysisDetails.builder()
                    .totalPosts(count)
                    .avgScore(scoreSum / count)
                    .firstPost(firstPost)
                    .lastPost(lastPost)
                    .build();
        }
    }

    static AnalysisDetails analyzePosts(Stream<StartElement> rowStream) {
        Collector<Post, PartialResult, AnalysisDetails> postStatisticsCollector = Collector.of(
                PartialResult::new,
                PartialResult::addPost,
                PartialResult::combine,
                PartialResult::finish
        );

        return rowStream
                .map(PostAnalyzer::toPost)
                .collect(postStatisticsCollector);
    }


    private static Post toPost(StartElement startElement) {
        return ImmutablePost.builder()
                .id(Integer.parseInt(getAttribute(startElement, "Id")))
                .score(Integer.parseInt(getAttribute(startElement, "Score")))
                .creationDate(LocalDateTime.parse(getAttribute(startElement, "CreationDate"), DateTimeFormatter.ISO_LOCAL_DATE_TIME).atOffset(ZoneOffset.UTC))
                .build();
    }

    private static String getAttribute(StartElement startElement, String attributeName) {
        return startElement.getAttributeByName(QName.valueOf(attributeName)).getValue();
    }
}
