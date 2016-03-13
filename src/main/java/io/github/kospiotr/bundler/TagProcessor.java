package io.github.kospiotr.bundler;

import org.apache.maven.plugin.logging.Log;

public abstract class TagProcessor {

    private ProcessMojo mojo;
    protected Log log;

    abstract String getType();

    abstract String process(Tag tag);

    public void setMojo(ProcessMojo mojo){
        this.mojo = mojo;
        this.log = mojo.getLog();
    }

    public ProcessMojo getMojo() {
        return mojo;
    }
}
