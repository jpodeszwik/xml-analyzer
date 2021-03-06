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
class XmlPostFileAnalysisServiceImpl implements XmlPostFileAnalysisService {
    private final XmlPostFileReader xmlPostFileReader;
    private final PostStreamAnalyzer postStreamAnalyzer;

    public XmlPostFileAnalysisServiceImpl(XmlPostFileReader xmlPostFileReader, PostStreamAnalyzer postStreamAnalyzer) {
        this.xmlPostFileReader = xmlPostFileReader;
        this.postStreamAnalyzer = postStreamAnalyzer;
    }

    public PostStats analyzeXmlPostFile(URL url) throws IOException, XMLStreamException {
        try (InputStream in = url.openStream()) {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(in);
            try {
                Stream<Post> posts = xmlPostFileReader.readPosts(xmlEventReader);
                return postStreamAnalyzer.analyzePosts(posts);
            } finally {
                xmlEventReader.close();
            }
        }
    }
}
