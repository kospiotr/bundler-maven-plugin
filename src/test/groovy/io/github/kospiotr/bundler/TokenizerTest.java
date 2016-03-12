package io.github.kospiotr.bundler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TokenizerTest {

    @Mock
    private RemoveTagProcessor tagProcessor;

    ArgumentCaptor<Tag> tagCaptor = ArgumentCaptor.forClass(Tag.class);

    @Before
    public void init() {
        when(tagProcessor.getType()).thenReturn("remove");
        when(tagProcessor.process(any(Tag.class))).thenReturn("");
    }

    @Test
    public void shouldThrowExceptionWhenNoSupportingTagProcessor() throws Exception {
        String content = "tagprefix<!-- bundle:remove attrib1  attrib2 -->tagcontent<!-- /bundle -->tagsuffix";
        Tokenizer tokenizer = new Tokenizer(null);
        try {
            tokenizer.process(content);
            fail("Exception should have been thrown");
        } catch (Exception e) {
            assertThat(e).hasMessage("Tag type: remove is not supported");
        }

    }

    @Test
    public void shouldNotProcessInvalidTagWithWrongTagBegin() throws Exception {
        String content = "tagprefix<!- - bundle:remove attrib1  attrib2 -->tagcontent<!-- /bundle -->tagsuffix";
        Tokenizer tokenizer = createTokenizerWithRemoveTagProcessor();

        assertThat(tokenizer.process(content)).isEqualTo(content);
    }

    @Test
    public void shouldNotProcessInvalidTagWithWrongTagName() throws Exception {
        String content = "tagprefix<!-- buundle:remove attrib1  attrib2 -->tagcontent<!-- /buundle -->tagsuffix";
        Tokenizer tokenizer = createTokenizerWithRemoveTagProcessor();

        assertThat(tokenizer.process(content)).isEqualTo(content);
    }

    @Test
    public void shouldNotProcessInvalidTagWithWrongTagClose() throws Exception {
        String content = "tagprefix<!-- buundle:remove attrib1  attrib2 -->tagcontent<!-- /buundle -->tagsuffix";
        Tokenizer tokenizer = createTokenizerWithRemoveTagProcessor();

        assertThat(tokenizer.process(content)).isEqualTo(content);
    }

    @Test
    public void shouldNotProcessInvalidTagWithoutTagClose() throws Exception {
        String content = "tagprefix<!-- buundle:remove attrib1  attrib2 -->tagcontenttagsuffix";
        Tokenizer tokenizer = createTokenizerWithRemoveTagProcessor();

        assertThat(tokenizer.process(content)).isEqualTo(content);
    }

    @Test
    public void shouldProcessInlineTag() throws Exception {

        String content = "tagprefix<!-- bundle:remove-->tagcontent<!-- /bundle -->tagsuffix";
        Tokenizer tokenizer = createTokenizerWithRemoveTagProcessor();

        assertThat(tokenizer.process(content)).isEqualTo("tagprefixtagsuffix");

        Tag capturedTag = getCapturedTag();
        assertThat(capturedTag.getType()).isEqualTo("remove");
        assertThat(capturedTag.getAttributes()).isEqualTo(new String[]{});
        assertThat(capturedTag.getContent()).isEqualTo("tagcontent");
    }

    @Test
    public void shouldProcessInlineTagWithAttributes() throws Exception {
        String content = "tagprefix<!-- bundle:remove attrib1  attrib2 -->tagcontent<!-- /bundle -->tagsuffix";
        Tokenizer tokenizer = createTokenizerWithRemoveTagProcessor();

        assertThat(tokenizer.process(content)).isEqualTo("tagprefixtagsuffix");

        Tag capturedTag = getCapturedTag();
        assertThat(capturedTag.getType()).isEqualTo("remove");
        assertThat(capturedTag.getAttributes()).isEqualTo(new String[]{"attrib1", "attrib2"});
        assertThat(capturedTag.getContent()).isEqualTo("tagcontent");
    }

    @Test
    public void shouldProcessInlineTagWithAttributesAndWhitespaces() throws Exception {
        String content = "tagprefix<!--      bundle:remove     attrib1      attrib2     -->tagcontent<!--     /bundle     -->tagsuffix";
        Tokenizer tokenizer = createTokenizerWithRemoveTagProcessor();

        assertThat(tokenizer.process(content)).isEqualTo("tagprefixtagsuffix");

        Tag capturedTag = getCapturedTag();
        assertThat(capturedTag.getType()).isEqualTo("remove");
        assertThat(capturedTag.getAttributes()).isEqualTo(new String[]{"attrib1", "attrib2"});
        assertThat(capturedTag.getContent()).isEqualTo("tagcontent");
    }

    @Test
    public void shouldProcessMultiLineTag() throws Exception {
        String content = "tagprefix\n<!-- bundle:remove attrib1  attrib2 -->\ntagc\nontent\n<!-- /bundle -->\ntagsuffix";
        Tokenizer tokenizer = createTokenizerWithRemoveTagProcessor();

        assertThat(tokenizer.process(content)).isEqualTo("tagprefix\n\ntagsuffix");

        Tag capturedTag = getCapturedTag();
        assertThat(capturedTag.getType()).isEqualTo("remove");
        assertThat(capturedTag.getAttributes()).isEqualTo(new String[]{"attrib1", "attrib2"});
        assertThat(capturedTag.getContent()).isEqualTo("\ntagc\nontent\n");
    }

    @Test
    public void shouldProcessMultipleTags() throws Exception {
        String content = "tag1prefix<!-- bundle:remove attrib11  attrib12 -->tagcontent1<!-- /bundle -->tagsuffix1\n" +
                "tag2prefix<!-- bundle:remove attrib21  attrib22 -->tagcontent2<!-- /bundle -->tagsuffix2";
        Tokenizer tokenizer = createTokenizerWithRemoveTagProcessor();

        assertThat(tokenizer.process(content)).isEqualTo("tag1prefixtagsuffix1\ntag2prefixtagsuffix2");

        List<Tag> capturedTags = getCapturedTags(2);
        Tag tag1 = capturedTags.get(0);
        assertThat(tag1.getType()).isEqualTo("remove");
        assertThat(tag1.getAttributes()).isEqualTo(new String[]{"attrib11", "attrib12"});
        assertThat(tag1.getContent()).isEqualTo("tagcontent1");
        Tag tag2 = capturedTags.get(1);
        assertThat(tag2.getType()).isEqualTo("remove");
        assertThat(tag2.getAttributes()).isEqualTo(new String[]{"attrib21", "attrib22"});
        assertThat(tag2.getContent()).isEqualTo("tagcontent2");
    }

    @Test
    public void shouldProcessMultipleMultilineTags() throws Exception {
        String content = "tag1prefix\n<!-- bundle:remove attrib11  attrib12 -->\ntag\ncontent1<!-- /bundle -->\ntagsuffix1\n" +
                "tag2prefix\n<!-- bundle:remove attrib21  attrib22 -->\ntag\ncontent2\n<!-- /bundle -->\ntagsuffix2\n";
        Tokenizer tokenizer = createTokenizerWithRemoveTagProcessor();

        assertThat(tokenizer.process(content)).isEqualTo("tag1prefix\n\ntagsuffix1\ntag2prefix\n\ntagsuffix2\n");

        List<Tag> capturedTags = getCapturedTags(2);
        Tag tag1 = capturedTags.get(0);
        assertThat(tag1.getType()).isEqualTo("remove");
        assertThat(tag1.getAttributes()).isEqualTo(new String[]{"attrib11", "attrib12"});
        assertThat(tag1.getContent()).isEqualTo("\ntag\ncontent1");
        Tag tag2 = capturedTags.get(1);
        assertThat(tag2.getType()).isEqualTo("remove");
        assertThat(tag2.getAttributes()).isEqualTo(new String[]{"attrib21", "attrib22"});
        assertThat(tag2.getContent()).isEqualTo("\ntag\ncontent2\n");
    }

    private Tokenizer createTokenizerWithRemoveTagProcessor() {
        Tokenizer tokenizer = new Tokenizer(null);
        tokenizer.registerProcessor(tagProcessor);
        return tokenizer;
    }

    private Tag getCapturedTag() {
        verify(tagProcessor).process(tagCaptor.capture());
        List<Tag> capturedTags = tagCaptor.getAllValues();
        assertThat(capturedTags).hasSize(1);
        return capturedTags.get(0);
    }

    private List<Tag> getCapturedTags(int tagCount) {
        verify(tagProcessor, times(tagCount)).process(tagCaptor.capture());
        return tagCaptor.getAllValues();
    }
}