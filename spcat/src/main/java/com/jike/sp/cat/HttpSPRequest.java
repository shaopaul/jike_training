package com.jike.sp.cat;

import com.jike.sp.cat.servlet.SPRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

public class HttpSPRequest implements SPRequest {

    private HttpRequest request;

    public HttpSPRequest(HttpRequest request) {
        this.request = request;
    }
    @Override
    public String getUri() {
        return request.uri();
    }

    @Override
    public String getPath() {
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        return decoder.path();
    }

    @Override
    public String getMethod() {
        return request.method().name();
    }

    @Override
    public Map<String, List<String>> getParameters() {
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        return decoder.parameters();
    }

    @Override
    public List<String> getParameters(String name) {
        return getParameters().get(name);
    }

    @Override
    public String getParameter(String name) {
        List<String> params = getParameters(name);
        if (params == null || params.size() == 0) {
            return null;
        }
        return params.get(0);
    }
}
