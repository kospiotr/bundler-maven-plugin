package com.github.kospiotr.bundler;

import java.io.File;

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
    private ResourceOptimizer resourceOptimizer = new ResourceOptimizer();

    @Override
    public String getType() {
        return "css";
    }


    @Override
    public String createBundledTag(String fileName) {
        return "<link rel=\"stylesheet\" href=\"" + fileName + "\" />";
    }

    @Override
    protected String postProcessOutputFileContent(String content) {
        return resourceOptimizer.optimizeCss(content);
    }

    @Override
    protected String tagRegex() {
        return TAG_REGEX;
    }

    @Override
    protected String preprocessTagContent(String scrContent, String src) {
        String srcParentPath = new File(src).getParent();
        return scrContent.replaceAll("url\\(", "url(" + srcParentPath);
    }
}
