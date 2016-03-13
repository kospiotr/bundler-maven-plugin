package io.github.kospiotr.bundler;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RegexBasedTagProcessor extends TagProcessor {

    private static final String HASH_PLACEHOLDER = "#hash#";
    private ResourceAccess resourceAccess = new ResourceAccess();

    /**
     * Construct tag which will be outputted as a result of bundle
     * @param fileName output fileName
     * @return output tag
     */
    protected abstract String createBundledTag(String fileName);

    /**
     * Regex that represents inner tags that will be processed and bundled.
     * It MUST return first capturing group which represents partial file path to read
     * @return inner tag regex
     */
    protected abstract String tagRegex();

    @Override
    public String process(Tag tag) {
        String fileName = extractFileName(tag);
        Path parentSrcPath = getMojo().getInputFilePah().getAbsoluteFile().toPath().getParent();
        String tagContent = tag.getContent();

        String content = processTags(parentSrcPath, tagContent);
        content = postProcessOutputFileContent(content);

        Path parentDestPath = getMojo().getOutputFilePath().getAbsoluteFile().toPath().getParent();
        if (fileName.contains(HASH_PLACEHOLDER)) {
            String hashValue = computeHash(content);
            fileName = fileName.replace(HASH_PLACEHOLDER, hashValue);
        }
        Path tagDestPath = parentDestPath.resolve(fileName);
        log.debug("Writing to file: " + tagDestPath);
        resourceAccess.write(tagDestPath, content);
        return createBundledTag(fileName);
    }

    /**
     * Template method allowing enhance output file content
     * @param content output file content
     * @return enhanced output file content
     */
    protected String postProcessOutputFileContent(String content){
        return content;
    }

    private String extractFileName(Tag tag) {
        String[] attributes = tag.getAttributes();
        String fileName = attributes == null || attributes.length == 0 ?
                null :
                attributes[0];

        if (fileName == null) {
            throw new IllegalArgumentException("File Name attribute is required");
        }

        return fileName;
    }

    private String processTags(Path parentSrcPath, String tagContent) {
        StringBuilder concatContent = new StringBuilder();
        Pattern tagPattern = Pattern.compile(tagRegex(), Pattern.DOTALL);
        Matcher m = tagPattern.matcher(tagContent);
        while (m.find()) {
            String src = m.group(1);
            Path tagSrcPath = parentSrcPath.resolve(src);
            String scrContent = resourceAccess.read(tagSrcPath);
            concatContent.append(scrContent).append("\n");
        }
        return concatContent.toString();
    }

    private String computeHash(String content) {
        try {
            MessageDigest md5 = MessageDigest.getInstance(getMojo().getHashingAlgorithm());
            byte[] digest = md5.digest(content.getBytes());
            return new HexBinaryAdapter().marshal(digest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
