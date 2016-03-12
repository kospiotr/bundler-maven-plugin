package io.github.kospiotr.bundler;

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
public class JsTagProcessor extends TagProcessor {

    private ResourceAccess resourceAccess = new ResourceAccess();

    @Override
    public String getType() {
        return "js";
    }

    @Override
    public String process(Tag tag) {
        String[] attributes = tag.getAttributes();
        String fileName = attributes == null || attributes.length == 0 ? null : attributes[0];
        if (fileName == null) {
            throw new IllegalArgumentException("File Name attribute is required");
        }
        Path parentSrcPath = getMojo().getInputFilePah().getAbsoluteFile().toPath().getParent();

        String content = tag.getContent();
        String regex = "\\Q<script\\E\\s*?src\\=\"(.*?)\"\\s*?\\>.*?\\Q</script>\\E";

        Pattern tagPattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher m = tagPattern.matcher(content);
        StringBuilder concatContent = new StringBuilder();
        while (m.find()) {
            log.info("Found match script tag");
            String src = m.group(1);
            Path tagSrcPath = parentSrcPath.resolve(src);
            String scrContent = resourceAccess.read(tagSrcPath);
            concatContent.append(scrContent).append("\n");
        }
        Path parentDestPath = getMojo().getOutputFilePath().getAbsoluteFile().toPath().getParent();
        Path tagDestPath = parentDestPath.resolve(fileName);
        log.debug("Writing to file: "+tagDestPath);
        resourceAccess.write(tagDestPath, concatContent.toString());
        return "<script src=\""+fileName+"\"></script>";
    }
}
