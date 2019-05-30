package com.github.kospiotr.bundler;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Usage:
 * <pre>{@code
 *     <!-- build:js inline app.min.js -->
 *     <script src="my/lib/path/lib.js"></script>
 *     <script src="my/deep/development/path/script.js"></script>
 *     <!-- /build -->
 *
 *     <!-- changed to -->
 *     <script>
 *     // app.min.js code here
 *     </script>
 * }
 * </pre>
 */
public class CssTagProcessor extends RegexBasedTagProcessor {

    private static final String TAG_REGEX = "\\Q<link\\E.*?href\\=\"(.*?)\".*?\\>";
    private PathNormalizator pathNormalizator = new PathNormalizator();

    @Override
    public String getType() {
        return "css";
    }


    @Override
    public String createBundledTag(String fileName) {
        return "<link rel=\"stylesheet\" href=\"" + fileName + "\" />";
    }

    @Override
    protected String tagRegex() {
        return TAG_REGEX;
    }

    @Override
    protected String preprocessTagContent(String targetCssPath, String content, String sourceCssPath) {
        StringBuilder sb = new StringBuilder();

        Pattern urlPattern = Pattern.compile("url\\([\\s'\"]*(.*?)[\\s'\"]*\\)", Pattern.DOTALL);
        Matcher m = urlPattern.matcher(content);
        int previousIndex = 0;
        while (m.find(previousIndex)) {
            String resourcePath = m.group(1);
            sb.append(content.substring(previousIndex, m.start()));
            String relativizedResourcePathUrl = relativizeResourcePath(targetCssPath, sourceCssPath, resourcePath);
            sb.append("url('").append(relativizedResourcePathUrl).append("')");
            previousIndex = m.end();
        }
        sb.append(content.substring(previousIndex, content.length()));
        return sb.toString();
    }

    private String relativizeResourcePath(String targetCssPath, String sourceCssPath, String resourcePath) {
        if (getMojo().preserveCssPaths()) {
            return resourcePath;
        }
        if (isUrlAbsolute(resourcePath)) {
            return resourcePath;
        } else {
            Path absoluteResourcePath = pathNormalizator.getAbsoluteResourcePath(getMojo().getInputFilePah().getAbsolutePath(),
                    sourceCssPath, resourcePath);
            Path absoluteTargetCssPath = pathNormalizator.getAbsoluteTargetCssPath(getMojo().getOutputFilePath().getAbsolutePath(),
                    targetCssPath);
            return pathNormalizator.relativize(absoluteResourcePath, absoluteTargetCssPath);
        }
    }

    private boolean isUrlAbsolute(String url) {
        return url.startsWith("/");
    }
}
