package com.github.kospiotr.bundler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CssTagProcessorTest {

    @Spy
    ProcessMojo processMojo = new ProcessMojo(new File("index-dev.html"), new File("index.html"));

    @Mock
    ResourceAccess resourceAccess;

    @InjectMocks
    CssTagProcessor cssTagProcessor;

    @Test
    public void shouldRejectWhenNoFileNameAttributeGiven() throws Exception {
        Tag jsTag = createCssTag("");

        try {
            cssTagProcessor.process(jsTag);
            fail("Should have thrown exception");
        } catch (Exception e) {
            assertThat(e).hasMessage("File Name attribute is required");
            verify(resourceAccess, never()).read(any(Path.class));
            verify(resourceAccess, never()).write(any(Path.class), any(String.class));
            verify(resourceAccess, never()).write(any(Path.class), any(String.class));
        }
    }

    @Test
    public void shouldProcessEmptyTag() throws Exception {
        Tag jsTag = createCssTag("", "app.css");
        String result = cssTagProcessor.process(jsTag);

        assertThat(result).isEqualTo("<link rel=\"stylesheet\" href=\"app.css\" />");
        verify(resourceAccess, never()).read(any(Path.class));
        verify(resourceAccess).write(argThat(new PathHamcrestMatcher("glob:**/app.css")), any(String.class));
    }

    @Test
    public void shouldProcessSingleTag() throws Exception {
        when(resourceAccess.read(any(Path.class))).thenReturn("");
        Tag jsTag = createCssTag("<link rel=\"stylesheet\" href=\"my/lib/path/lib.css\" />", "app.css");
        String result = cssTagProcessor.process(jsTag);

        assertThat(result).isEqualTo("<link rel=\"stylesheet\" href=\"app.css\" />");
        verify(resourceAccess).read(argThat(new PathHamcrestMatcher("glob:**/lib.css")));
        verify(resourceAccess).write(argThat(new PathHamcrestMatcher("glob:**/app.css")), any(String.class));
    }

    @Test
    public void shouldProcessMultipleInlineTags() throws Exception {
        when(resourceAccess.read(any(Path.class))).thenReturn("");
        Tag jsTag = createCssTag("<link href=\"my/lib/path/lib1.css\" /><link href=\"my/lib/path/lib2.css\" />", "app.css");
        String result = cssTagProcessor.process(jsTag);

        assertThat(result).isEqualTo("<link rel=\"stylesheet\" href=\"app.css\" />");
        verify(resourceAccess).read(argThat(new PathHamcrestMatcher("glob:**/lib1.css")));
        verify(resourceAccess).read(argThat(new PathHamcrestMatcher("glob:**/lib2.css")));
        verify(resourceAccess).write(argThat(new PathHamcrestMatcher("glob:**/app.css")), any(String.class));
    }

    @Test
    public void shouldProcessMultipleMultiLineTags() throws Exception {
        when(resourceAccess.read(any(Path.class))).thenReturn("");
        Tag jsTag = createCssTag("<link href=\"my/lib/path/lib1.css\" />\n<!-- sample comment -->\n<link href=\"my/lib/path/lib2.css\" />", "app.css");
        String result = cssTagProcessor.process(jsTag);

        assertThat(result).isEqualTo("<link rel=\"stylesheet\" href=\"app.css\" />");
        verify(resourceAccess).read(argThat(new PathHamcrestMatcher("glob:**/lib1.css")));
        verify(resourceAccess).read(argThat(new PathHamcrestMatcher("glob:**/lib2.css")));
        verify(resourceAccess).write(argThat(new PathHamcrestMatcher("glob:**/app.css")), any(String.class));
    }

    @Test
    public void shouldNormalizePathsWhenProcessingFilesFromDifferentPathLevels() throws Exception {
        when(resourceAccess.read(argThat(new PathHamcrestMatcher("glob:**/lib1.css"))))
                .thenReturn("h1 {background-image: url(\"paper1.gif\");}\n" +
                        "h2 {background-image: url(../paper2.gif);}\n" +
                        "h3 {background-image: url('app/paper3.gif');}");
        when(resourceAccess.read(argThat(new PathHamcrestMatcher("glob:**/lib2.css"))))
                .thenReturn("h4 {background-image: url( \"paper4.gif\" );}\n" +
                        "h5 {background-image: url( ../paper5.gif);}\n" +
                        "h6 {background-image: url('app/paper6.gif' );}");
        when(resourceAccess.read(argThat(new PathHamcrestMatcher("glob:**/lib3.css"))))
                .thenReturn("h7 {background-image: url('/paper7.gif');}");

        Tag jsTag = createCssTag("<link href=\"../lib1.css\" /><link href=\"lib2.css\" /><link href=\"lib/lib3.css\" />", "app.css");
        String result = cssTagProcessor.process(jsTag);

        assertThat(result).isEqualTo("<link rel=\"stylesheet\" href=\"app.css\" />");
        verify(resourceAccess).read(argThat(new PathHamcrestMatcher("glob:**/lib1.css")));
        verify(resourceAccess).read(argThat(new PathHamcrestMatcher("glob:**/lib2.css")));
        verify(resourceAccess).read(argThat(new PathHamcrestMatcher("glob:**/lib3.css")));
        verify(resourceAccess).write(argThat(new PathHamcrestMatcher("glob:**/app.css")), eq(
                "h1 {background-image: url('../paper1.gif');}\n" +
                        "h2 {background-image: url('../../paper2.gif');}\n" +
                        "h3 {background-image: url('../app/paper3.gif');}\n" +
                        "h4 {background-image: url('paper4.gif');}\n" +
                        "h5 {background-image: url('../paper5.gif');}\n" +
                        "h6 {background-image: url('app/paper6.gif');}\n" +
                        "h7 {background-image: url('/paper7.gif');}\n"));
    }

    private Tag createCssTag(String content, String... attributes) {
        return new Tag(content, "css", attributes);
    }

}