package org.xi.quick.model;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

public class RequestModel<T> {

    public RequestModel() {
    }

    public RequestModel(T data) {
        postBody = JSON.toJSONString(data);
    }

    private String encoding = "utf-8";
    private String postBody = "";
    private Map<String, String> paramMap = new HashMap<>();
    private Map<String, String> headerMap = new HashMap<>();

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getPostBody() {
        return postBody;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public Map<String, String> putParam(String key, String value) {
        paramMap.put(key, value);
        return paramMap;
    }

    public Map<String, String> putHeader(String key, String value) {
        headerMap.put(key, value);
        return headerMap;
    }
}
