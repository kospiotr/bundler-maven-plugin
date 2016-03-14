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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CssTagProcessorTest {

    @Spy
    ProcessMojo processMojo = new ProcessMojo(new File("index-dev.html"), new File("index.html"));

    @Mock
    ResourceAccess resourceAccess;

    @Mock
    ResourceOptimizer resourceOptimizer;

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
            verify(resourceOptimizer, never()).optimizeCss(any(String.class));
        }
    }

    @Test
    public void shouldProcessEmptyTag() throws Exception {
        Tag jsTag = createCssTag("", "app.css");
        String result = cssTagProcessor.process(jsTag);

        assertThat(result).isEqualTo("<link rel=\"stylesheet\" href=\"app.css\" />");
        verify(resourceAccess, never()).read(any(Path.class));
        verify(resourceAccess).write(argThat(new PathHamcrestMatcher("glob:**/app.css")), any(String.class));
        verify(resourceOptimizer).optimizeCss(any(String.class));
    }

    @Test
    public void shouldProcessSingleTag() throws Exception {
        Tag jsTag = createCssTag("<link rel=\"stylesheet\" href=\"my/lib/path/lib.css\" />", "app.css");
        String result = cssTagProcessor.process(jsTag);

        assertThat(result).isEqualTo("<link rel=\"stylesheet\" href=\"app.css\" />");
        verify(resourceAccess).read(argThat(new PathHamcrestMatcher("glob:**/lib.css")));
        verify(resourceAccess).write(argThat(new PathHamcrestMatcher("glob:**/app.css")), any(String.class));
        verify(resourceOptimizer).optimizeCss(any(String.class));
    }

    @Test
    public void shouldProcessMultipleInlineTags() throws Exception {
        Tag jsTag = createCssTag("<link href=\"my/lib/path/lib1.css\" /><link href=\"my/lib/path/lib2.css\" />", "app.css");
        String result = cssTagProcessor.process(jsTag);

        assertThat(result).isEqualTo("<link rel=\"stylesheet\" href=\"app.css\" />");
        verify(resourceAccess).read(argThat(new PathHamcrestMatcher("glob:**/lib1.css")));
        verify(resourceAccess).read(argThat(new PathHamcrestMatcher("glob:**/lib2.css")));
        verify(resourceAccess).write(argThat(new PathHamcrestMatcher("glob:**/app.css")), any(String.class));
        verify(resourceOptimizer).optimizeCss(any(String.class));
    }

    @Test
    public void shouldProcessMultipleMultiLineTags() throws Exception {
        Tag jsTag = createCssTag("<link href=\"my/lib/path/lib1.css\" />\n<!-- sample comment -->\n<link href=\"my/lib/path/lib2.css\" />", "app.css");
        String result = cssTagProcessor.process(jsTag);

        assertThat(result).isEqualTo("<link rel=\"stylesheet\" href=\"app.css\" />");
        verify(resourceAccess).read(argThat(new PathHamcrestMatcher("glob:**/lib1.css")));
        verify(resourceAccess).read(argThat(new PathHamcrestMatcher("glob:**/lib2.css")));
        verify(resourceAccess).write(argThat(new PathHamcrestMatcher("glob:**/app.css")), any(String.class));
        verify(resourceOptimizer).optimizeCss(any(String.class));
    }

    private Tag createCssTag(String content, String... attributes) {
        return new Tag(content, "css", attributes);
    }

}