package pl.jp.analyzer.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.stream.Stream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import org.springframework.stereotype.Service;

@Service
public class XmlPostFileAnalysisService {
    public AnalysisDetails analyze(URL url) throws IOException, XMLStreamException {
        try (InputStream in = url.openStream()) {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(in);
            try {
                Stream<StartElement> rowStream = XmlRowReader.rowStream(xmlEventReader);
                return PostAnalyzer.analyzePosts(rowStream);
            } finally {
                xmlEventReader.close();
            }
        }
    }
}
