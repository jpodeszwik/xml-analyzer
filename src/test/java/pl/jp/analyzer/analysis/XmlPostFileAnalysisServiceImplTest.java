package pl.jp.analyzer.analysis;

import com.google.common.io.Resources;
import java.io.IOException;
import java.util.stream.Stream;
import javax.xml.stream.XMLStreamException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.jp.analyzer.analysis.TestData.POST_1;
import static pl.jp.analyzer.analysis.TestData.POST_2;

@RunWith(MockitoJUnitRunner.class)
public class XmlPostFileAnalysisServiceImplTest {
    @Mock
    private PostStreamAnalyzer postStreamAnalyzer;

    @Mock
    private XmlPostFileReader xmlPostFileReader;

    @InjectMocks
    private XmlPostFileAnalysisServiceImpl xmlPostFileAnalysisService;

    @Test
    public void analyzePostXmlFile() throws IOException, XMLStreamException {
        // given
        var url = Resources.getResource("two-posts.xml");
        var postStats = ImmutablePostStats.builder().totalPosts(2).firstPost(POST_1.creationDate()).lastPost(POST_2.creationDate())
                .avgScore((POST_1.score() + POST_2.score()) / 2).build();
        when(xmlPostFileReader.readPosts(any())).thenReturn(Stream.of(POST_1, POST_2));
        when(postStreamAnalyzer.analyzePosts(any())).thenReturn(postStats);

        // when
        var returnedPostStats = xmlPostFileAnalysisService.analyzeXmlPostFile(url);

        // then
        assertThat(returnedPostStats).isEqualTo(postStats);
        verify(xmlPostFileReader).readPosts(any());
        verify(postStreamAnalyzer).analyzePosts(any());
        verifyNoMoreInteractions(xmlPostFileReader, postStreamAnalyzer);
    }
}