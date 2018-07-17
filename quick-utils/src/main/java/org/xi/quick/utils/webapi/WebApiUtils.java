package org.xi.quick.utils.webapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.xi.quick.model.RequestModel;
import org.xi.quick.utils.net.WebRequestUtils;

import java.io.IOException;

public class WebApiUtils {

    /**
     * 从 WEB API 接口返回对象
     *
     * @param url           请求地址
     * @param requestType   请求类型
     * @param typeReference 返回类型引用
     * @param <T>           返回类型
     * @return
     * @throws IOException
     */
    public static <T> T getResult(String url, WebRequestUtils.RequestType requestType, TypeReference<T> typeReference) throws IOException {

        String responseResult;
        switch (requestType) {
            case GET:
                responseResult = WebRequestUtils.get(url);
                break;
            case POST:
                responseResult = WebRequestUtils.get(url);
                break;
            default:
                responseResult = "";
                break;
        }
        if (StringUtils.isBlank(responseResult)) return null;
        return JSON.parseObject(responseResult, typeReference);
    }

    /**
     * 从 WEB API 接口返回对象
     *
     * @param url           请求地址
     * @param requestType   请求类型
     * @param requestModel  请求model
     * @param typeReference 返回类型引用
     * @param <T>           返回类型
     * @return
     * @throws IOException
     */
    public static <T> T getResult(String url, WebRequestUtils.RequestType requestType, RequestModel requestModel, TypeReference<T> typeReference) throws IOException {

        if (requestModel == null) return getResult(url, requestType, typeReference);

        String responseResult;
        switch (requestType) {
            case GET:
                responseResult = WebRequestUtils.get(url, requestModel.getParamMap(), requestModel.getHeaderMap(), requestModel.getEncoding());
                break;
            case POST:
                responseResult = WebRequestUtils.post(url, requestModel.getParamMap(), requestModel.getHeaderMap(), requestModel.getPostBody(), requestModel.getEncoding());
                break;
            default:
                responseResult = "";
                break;
        }
        if (StringUtils.isBlank(responseResult)) return null;
        return JSON.parseObject(responseResult, typeReference);
    }
}
