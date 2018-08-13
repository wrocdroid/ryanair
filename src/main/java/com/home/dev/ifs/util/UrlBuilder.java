package com.home.dev.ifs.util;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class UrlBuilder {

    public String constructUrlWithParams(String template, Map<String, String> params) {
        return UriComponentsBuilder.fromUriString(template).buildAndExpand(params).toUriString();
    }
}
