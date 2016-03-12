package io.github.kospiotr.bundler

class Tag {
    String content;
    String type;
    String[] attributes;

    Tag(String content, String type, String[] attributes) {
        this.content = content;
        this.type = type;
        this.attributes = attributes;
    }


    @Override
    public String toString() {
        return """\
Tag{
    content='$content',
    type='$type',
    attributes=${Arrays.toString(attributes)}
}"""
    }
}
