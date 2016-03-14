package com.github.kospiotr.bundler;

public class RemoveTagProcessor extends TagProcessor{

    @Override
    public String getType() {
        return "remove";
    }

    @Override
    public String process(Tag tag) {
        return "";
    }
}
