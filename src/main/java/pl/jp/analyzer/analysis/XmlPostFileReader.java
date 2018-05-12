package pl.jp.analyzer.analysis;

import com.google.common.collect.Streams;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.stream.Stream;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

class XmlPostFileReader {
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

    static Stream<Post> readPosts(XMLEventReader xmlEventReader) {
        EventIterator eventIterator = new EventIterator(xmlEventReader);
        Stream<XMLEvent> targetStream = Streams.stream(eventIterator);

        return targetStream.filter(XMLEvent::isStartElement)
                .map(XMLEvent::asStartElement)
                .filter(startElement -> "row".equals(startElement.getName().getLocalPart()))
                .map(XmlPostFileReader::toPost);

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
