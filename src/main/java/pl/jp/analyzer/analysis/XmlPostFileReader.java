package pl.jp.analyzer.analysis;

import com.google.common.collect.Streams;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.springframework.stereotype.Component;

@Component
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
                throw new XmlFileException("Unexpected problem while reading Xml event", e);
            }
        }
    }

    Stream<Post> readPosts(XMLEventReader xmlEventReader) {
        EventIterator eventIterator = new EventIterator(xmlEventReader);
        Stream<XMLEvent> targetStream = Streams.stream(eventIterator);

        return targetStream.filter(XMLEvent::isStartElement)
                .map(XMLEvent::asStartElement)
                .filter(startElement -> "row".equals(startElement.getName().getLocalPart()))
                .map(XmlPostFileReader::toPost);
    }

    private static Post toPost(StartElement startElement) {
        OffsetDateTime creationDate = LocalDateTime.parse(
                getAttribute(startElement, "CreationDate"),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
        ).atOffset(ZoneOffset.UTC);

        return ImmutablePost.builder()
                .id(getIntAttribute(startElement, "Id"))
                .score(getIntAttribute(startElement, "Score"))
                .creationDate(creationDate)
                .acceptedAnswerId(getNullableIntegerAttribute(startElement, "AcceptedAnswerId"))
                .build();
    }

    private static String getAttribute(StartElement startElement, String attributeName) {
        return getOptionalAttribute(startElement, attributeName)
                .orElseThrow(() -> new IllegalStateException("Attribute " + attributeName + " could not be found"));
    }

    private static Optional<String> getOptionalAttribute(StartElement startElement, String attributeName) {
        return Optional.of(QName.valueOf(attributeName))
                .map(startElement::getAttributeByName)
                .map(Attribute::getValue);
    }

    private static int getIntAttribute(StartElement startElement, String attributeName) {
        String attributeValue = getAttribute(startElement, attributeName);
        return Integer.parseInt(attributeValue);
    }

    private static Integer getNullableIntegerAttribute(StartElement startElement, String attributeName) {
        return getOptionalAttribute(startElement, attributeName)
                .map(Integer::parseInt)
                .orElse(null);
    }
}
