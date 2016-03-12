package io.github.kospiotr.bundler;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RemoveTagProcessorTest {

    @Test
    public void shouldAlwaysReturnEmptyContent() throws Exception {
        assertThat(new RemoveTagProcessor().process(null)).isEqualTo("");
        assertThat(new RemoveTagProcessor().process(new Tag("", "", null))).isEqualTo("");
        assertThat(new RemoveTagProcessor().process(new Tag("", "", new String[]{}))).isEqualTo("");
        assertThat(new RemoveTagProcessor().process(new Tag("a", "b", new String[]{"c"}))).isEqualTo("");
    }
}