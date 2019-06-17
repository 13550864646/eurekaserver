package com.cloud.mina.util;

import com.sun.xml.internal.ws.api.pipe.TransportTubeFactory;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import java.util.Arrays;

/**
 *  http 客户端
 */
public class HttpClientUtil {
    private static org.apache.log4j.Logger log = Logger.getLogger(C3P0Util.class);
    public static boolean sendHttpData(String className, String url, NameValuePair[] parameter) {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(url);
        boolean isSuccess = true;
        try {
            post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            post.setRequestBody(parameter);
            log.info(className + "send data :" + Arrays.deepToString(parameter));
//          设置连接超时时间
            client.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
//          设置响应超时时间
            client.getHttpConnectionManager().getParams().setSoTimeout(15000);
            int returnFlag = client.executeMethod(post);
            if (returnFlag != 200) {
                isSuccess = false;
            }
            log.info(className + "success receive form post:" + post.getStatusLine().toString() + ",returnFlag=" + returnFlag);
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
            log.info(className + "fail receive form post :" + e.getMessage());
        } finally {
            if (post != null) {
                post.releaseConnection();
                log.info(className + "post.releaseConnection()" + "is coming");
            }
        }
        return isSuccess;
    }
}
