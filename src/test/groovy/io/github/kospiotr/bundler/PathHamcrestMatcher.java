package io.github.kospiotr.bundler;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;

public class PathHamcrestMatcher extends ArgumentMatcher<Path> {

    private final String matcher;

    public PathHamcrestMatcher(String matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean matches(Object actual) {
        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(matcher);
        return pathMatcher.matches((Path) actual);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(matcher == null ? null : matcher);
    }
}
