package com.nedap.archie.aom;

import java.util.Map;

/**
 * Created by pieter.bos on 02/11/15.
 */
public class ResourceAnnotations {
    //TODO: write more convenient methods than this very deep map
    private Map<String, Map<String, Map<String, String>>> documentation;

    public Map<String, Map<String, Map<String, String>>> getDocumentation() {
        return documentation;
    }

    public void setDocumentation(Map<String, Map<String, Map<String, String>>> documentation) {
        this.documentation = documentation;
    }
}