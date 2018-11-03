package com.traderdiary.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.traderdiary.rest.config.ExtendedContextResolver;

public class JSONUtils {

    public static ObjectNode objectNode(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(ExtendedContextResolver.configureModule());
        return mapper.convertValue(object, ObjectNode.class);
    }
}
