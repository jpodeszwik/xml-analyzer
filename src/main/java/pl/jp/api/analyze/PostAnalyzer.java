package pl.jp.api.analyze;

import com.google.common.collect.Streams;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Stream;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class PostAnalyzer {

    private static final class EventIterator implements Iterator<XMLEvent> {
        private final XMLEventReader xmlEventReader;

        private EventIterator(XMLEventReader xmlEventReader) {
            this.xmlEventReader = xmlEventReader;
        }

        @Override
        public boolean hasNext() {
            return xmlEventReader.hasNext();
        }

        @Override
        public XMLEvent next() {
            try {
                return xmlEventReader.nextEvent();
            } catch (XMLStreamException e) {
                throw new IllegalStateException("Unexpected problem while reading Xml event", e);
            }
        }
    }

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

        private AnalyzeDetails finish() {
            return ImmutableAnalyzeDetails.builder()
                    .totalPosts(count)
                    .avgScore(scoreSum / count)
                    .firstPost(firstPost)
                    .lastPost(lastPost)
                    .build();
        }
    }

    public static AnalyzeDetails analyze(InputStream in) throws XMLStreamException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        var xmlEventReader = xmlInputFactory.createXMLEventReader(in);
        var eventIterator = new EventIterator(xmlEventReader);
        Stream<XMLEvent> targetStream = Streams.stream(eventIterator);
        Stream<Post> posts = targetStream.filter(XMLEvent::isStartElement)
                .map(XMLEvent::asStartElement)
                .filter(startElement -> "row".equals(startElement.getName().getLocalPart()))
                .map(PostAnalyzer::toPost);

        Collector<Post, PartialResult, AnalyzeDetails> postCollector = Collector.of(
                PartialResult::new,
                PartialResult::addPost,
                PartialResult::combine,
                PartialResult::finish
        );

        return posts.collect(postCollector);
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
