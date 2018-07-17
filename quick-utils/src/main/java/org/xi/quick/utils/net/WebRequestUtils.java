package org.xi.quick.utils.net;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.xi.quick.utils.web.UrlUtil;

import java.io.IOException;
import java.util.Map;

public class WebRequestUtils {

    public enum RequestType {
        GET, POST
    }

    public static final String DEFAULT_ENCODING = "utf-8";

    /**
     * 返回web请求的数据
     *
     * @param url 请求地址
     * @return 返回的数据
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        return get(url, null, null, DEFAULT_ENCODING);
    }

    /**
     * 返回web请求的数据
     *
     * @param url      请求地址
     * @param encoding 编码方式
     * @return 返回的数据
     * @throws IOException
     */
    public static String get(String url, String encoding) throws IOException {
        return get(url, null, null, encoding);
    }

    /**
     * 返回web请求的数据
     *
     * @param url     请求地址
     * @param headers 请求头集合
     * @return 返回的数据
     * @throws IOException
     */
    public static String get(String url, Map<String, String> headers) throws IOException {
        return get(url, null, headers, DEFAULT_ENCODING);
    }

    /**
     * 返回web请求的数据
     *
     * @param url      请求地址
     * @param headers  请求头集合
     * @param encoding 编码方式
     * @return 返回的数据
     * @throws IOException
     */
    public static String get(String url, Map<String, String> headers, String encoding) throws IOException {
        return get(url, null, headers, encoding);
    }

    /**
     * 返回web请求的数据
     *
     * @param url        请求地址
     * @param parameters 请求参数集合
     * @param headers    请求头集合
     * @return 返回的数据
     * @throws IOException
     */
    public static String get(String url, Map<String, String> parameters, Map<String, String> headers) throws IOException {
        return get(url, parameters, headers, DEFAULT_ENCODING);
    }

    /**
     * 返回web请求的数据
     *
     * @param url        请求地址
     * @param parameters 请求参数集合
     * @param headers    请求头集合
     * @param encoding   编码方式
     * @return 返回的数据
     * @throws IOException
     */
    public static String get(String url, Map<String, String> parameters, Map<String, String> headers, String encoding) throws IOException {

        if (StringUtils.isBlank(url)) return "";
        url = UrlUtil.getUrl(url, parameters);

        HttpGet httpGet = new HttpGet(url);
        setHeaders(httpGet, headers);

        return getHtml(httpGet, encoding);
    }

    /**
     * 返回web请求的数据
     *
     * @param url      请求地址
     * @param postBody POST提交的数据
     * @return 返回的数据
     * @throws IOException
     */
    public static String post(String url, String postBody) throws IOException {
        return post(url, null, null, postBody, DEFAULT_ENCODING);
    }

    /**
     * 返回web请求的数据
     *
     * @param url      请求地址
     * @param postBody POST提交的数据
     * @param encoding 编码方式
     * @return 返回的数据
     * @throws IOException
     */
    public static String post(String url, String postBody, String encoding) throws IOException {
        return post(url, null, null, postBody, encoding);
    }

    /**
     * 返回web请求的数据
     *
     * @param url        请求地址
     * @param parameters 请求参数集合
     * @param postBody   POST提交的数据
     * @return 返回的数据
     * @throws IOException
     */
    public static String post(String url, Map<String, String> parameters, String postBody) throws IOException {
        return post(url, parameters, null, postBody, DEFAULT_ENCODING);
    }

    /**
     * 返回web请求的数据
     *
     * @param url        请求地址
     * @param parameters 请求参数集合
     * @param postBody   POST提交的数据
     * @param encoding   编码方式
     * @return 返回的数据
     * @throws IOException
     */
    public static String post(String url, Map<String, String> parameters, String postBody, String encoding) throws IOException {
        return post(url, parameters, null, postBody, encoding);
    }

    /**
     * 返回web请求的数据
     *
     * @param url        请求地址
     * @param parameters 请求参数集合
     * @param headers    请求头集合
     * @param postBody   POST提交的数据
     * @return 返回的数据
     * @throws IOException
     */
    public static String post(String url, Map<String, String> parameters, Map<String, String> headers, String postBody) throws IOException {
        return post(url, parameters, headers, postBody, DEFAULT_ENCODING);
    }

    /**
     * 返回web请求的数据
     *
     * @param url        请求地址
     * @param parameters 请求参数集合
     * @param headers    请求头集合
     * @param postBody   POST提交的数据
     * @param encoding   编码方式
     * @return 返回的数据
     * @throws IOException
     */
    public static String post(String url, Map<String, String> parameters, Map<String, String> headers, String postBody, String encoding) throws IOException {

        if (StringUtils.isBlank(url)) return "";
        url = UrlUtil.getUrl(url, parameters);

        HttpPost httpPost = new HttpPost(url);
        setHeaders(httpPost, headers);

        if (!StringUtils.isBlank(postBody)) {
            httpPost.setEntity(new StringEntity(postBody, encoding));
        }

        return getHtml(httpPost, encoding);
    }

    /**
     * 设置Header
     *
     * @param request
     * @param headers
     */
    private static void setHeaders(HttpUriRequest request, Map<String, String> headers) {

        if (null != headers && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 获取响应字符串
     *
     * @param request
     * @param encoding
     * @return
     * @throws IOException
     */
    private static String getHtml(HttpUriRequest request, String encoding) throws IOException {

        if (StringUtils.isBlank(encoding)) encoding = DEFAULT_ENCODING;

        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(request);
        String html = EntityUtils.toString(response.getEntity(), encoding);
        return html;
    }
}
