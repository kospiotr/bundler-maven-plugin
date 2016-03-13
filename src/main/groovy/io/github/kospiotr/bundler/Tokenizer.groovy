package io.github.kospiotr.bundler

import groovy.util.logging.Log

import java.util.regex.Matcher
import java.util.regex.Pattern

@Log
public class Tokenizer {

    public static final String DEFAULT_TAG_START = "<!--"
    public static final String DEFAULT_TAG_END = "-->"
    public static final String DEFAULT_TAG_NAME = "bundle"
    public static final String DEFAULT_SEPARATOR = ":"

    private final ProcessMojo mojo;
    final String tagStart
    final String tagEnd
    final String tagName
    final String separator
    private Map<String, TagProcessor> tagProcessors = new HashMap<>()


    public Tokenizer(ProcessMojo mojo) {
        this.mojo = mojo;
        this.tagStart = DEFAULT_TAG_START
        this.tagEnd = DEFAULT_TAG_END
        this.tagName = DEFAULT_TAG_NAME
        this.separator = DEFAULT_SEPARATOR
    }

    public void registerProcessor(TagProcessor tagProcessor) {
        String tagType = tagProcessor.getType()
        if (this.tagProcessors.containsKey(tagType)) {
            throw new IllegalStateException("Processor for tag type: '" + tagType + "' is already registered")
        }
        this.tagProcessors.put(tagType, tagProcessor)
        tagProcessor.setMojo(mojo);
    }

    public String process(String content) {
        StringBuilder sb = new StringBuilder()
        String regex = "\\Q" + tagStart + "\\E\\s*\\Q" + tagName + separator+"\\E([^ ]*)\\s*(.*?)\\Q" + tagEnd + "\\E" +
                "(.*?)" +
                "\\Q" + tagStart + "\\E\\s*\\Q/" + tagName + "\\E\\s*\\Q" + tagEnd + "\\E"
        log.info "regex = $regex"
        Pattern tagPattern = Pattern.compile(regex, Pattern.DOTALL)
        Matcher m = tagPattern.matcher(content)
        int previousIndex = 0
        log.info "Content to process: $content"
        while (m.find(previousIndex)) {
            String type = m.group(1)
            String attributeContent = m.group(2)
            String tagContent = m.group(3)
            String[] attributes = extractAttributes(attributeContent)
            Tag tag = new Tag(tagContent, type, attributes)
            log.info "Found tag: $tag"

            sb.append(content.substring(previousIndex, m.start()))
            TagProcessor processor = tagProcessors.get(tag.getType())
            if (processor == null) {
                throw new IllegalArgumentException("Tag type: " + tag.getType() + " is not supported")
            }

            sb.append(processor.process(tag))
            previousIndex = m.end()
        }
        sb.append(content.substring(previousIndex, content.length()))

        String out = sb.toString()
        log.info "Processed content: $out"
        return out
    }

    static def String[] extractAttributes(String attributeContent) {
        return attributeContent == null || "".equals(attributeContent) ?
                [] :
                attributeContent.split("\\s+")
    }

}