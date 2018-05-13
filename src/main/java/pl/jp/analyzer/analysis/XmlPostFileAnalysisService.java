package pl.jp.analyzer.analysis;

import java.io.IOException;
import java.net.URL;
import javax.xml.stream.XMLStreamException;

public interface XmlPostFileAnalysisService {
    PostStats analyzeXmlPostFile(URL url) throws IOException, XMLStreamException;
}
