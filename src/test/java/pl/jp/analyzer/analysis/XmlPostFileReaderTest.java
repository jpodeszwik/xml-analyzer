package pl.jp.analyzer.analysis;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.jp.analyzer.analysis.TestData.POST_1;
import static pl.jp.analyzer.analysis.TestData.POST_2;

public class XmlPostFileReaderTest {
    private XmlPostFileReader xmlPostFileReader = new XmlPostFileReader();
    private XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

    @Test
    public void forEmptyPosts_shouldReturnEmptyStream() throws IOException, XMLStreamException {
        // given
        InputStream inputStream = Resources.getResource("zero-posts.xml").openStream();
        XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);

        // when
        Stream<Post> postStream = xmlPostFileReader.readPosts(xmlEventReader);

        // then
        assertThat(postStream).isEmpty();
    }

    @Test
    public void forOnePost_shouldReturnSingleElement() throws IOException, XMLStreamException {
        // given
        InputStream inputStream = Resources.getResource("one-post.xml").openStream();
        XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);

        // when
        Stream<Post> postStream = xmlPostFileReader.readPosts(xmlEventReader);

        // then
        assertThat(postStream).isEqualTo(ImmutableList.of(POST_1));
    }

    @Test
    public void forTwoPosts_shouldReturnTwoElements() throws IOException, XMLStreamException {
        // given
        InputStream inputStream = Resources.getResource("two-posts.xml").openStream();
        XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);

        // when
        Stream<Post> postStream = xmlPostFileReader.readPosts(xmlEventReader);

        // then
        assertThat(postStream).isEqualTo(ImmutableList.of(POST_1, POST_2));
    }

    @Test(expected = XmlFileException.class)
    public void forInvalidXmlFile_shouldThrowException() throws IOException, XMLStreamException {
        // given
        InputStream inputStream = Resources.getResource("invalid-file.xml").openStream();
        XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);

        // when
        Stream<Post> postStream = xmlPostFileReader.readPosts(xmlEventReader);
        postStream.count(); // consume stream

        // then
    }
}