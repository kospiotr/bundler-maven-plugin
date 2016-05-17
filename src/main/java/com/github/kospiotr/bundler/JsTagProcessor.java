package com.github.kospiotr.bundler;

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
public class JsTagProcessor extends RegexBasedTagProcessor {

    private static final String TAG_REGEX = "\\Q<script\\E\\s*?src\\=\"(.*?)\"\\s*?\\>.*?\\Q</script>\\E";

    @Override
    public String getType() {
        return "js";
    }

    @Override
    public String createBundledTag(String fileName) {
        return "<script src=\"" + fileName + "\"></script>";
    }

    @Override
    protected String tagRegex() {
        return TAG_REGEX;
    }

}
