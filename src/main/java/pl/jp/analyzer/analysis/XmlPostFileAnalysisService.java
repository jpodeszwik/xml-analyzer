package pl.jp.analyzer.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.stream.Stream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import org.springframework.stereotype.Service;

@Service
public class XmlPostFileAnalysisService {
    public AnalysisDetails analyze(URL url) throws IOException, XMLStreamException {
        try (InputStream in = url.openStream()) {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(in);
            try {
                Stream<Post> posts = XmlPostFileReader.readPosts(xmlEventReader);
                return PostAnalyzer.analyzePosts(posts);
            } finally {
                xmlEventReader.close();
            }
        }
    }
}
