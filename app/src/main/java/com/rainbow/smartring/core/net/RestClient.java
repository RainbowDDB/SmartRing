package com.rainbow.smartring.core.net;

import com.rainbow.smartring.core.net.callback.IError;
import com.rainbow.smartring.core.net.callback.IFailure;
import com.rainbow.smartring.core.net.callback.IRequest;
import com.rainbow.smartring.core.net.callback.ISuccess;
import com.rainbow.smartring.core.net.callback.RequestCallback;

import java.util.Map;
import java.util.WeakHashMap;

import retrofit2.Call;

/**
 * SuperSchedule
 * Created By Rainbow on 2020/5/5.
 */
public class RestClient {
    private final WeakHashMap<String, Object> params = RestCreator.getParams();
    private String url;
    private ISuccess iSuccess;
    private IFailure iFailure;
    private IRequest iRequest;
    private IError iError;

    private RestClient(String url,
                       Map<String, Object> params,
                       ISuccess success,
                       IError error,
                       IRequest request,
                       IFailure failure) {
        this.url = url;
        this.params.putAll(params);
        this.iError = error;
        this.iFailure = failure;
        this.iRequest = request;
        this.iSuccess = success;
    }

    public static class Builder {
        private String url;
        private Map<String, Object> params = RestCreator.getParams();
        private ISuccess success;
        private IFailure failure;
        private IRequest request;
        private IError error;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder params(WeakHashMap<String, Object> params) {
            this.params.putAll(params);
            return this;
        }

        public Builder param(String key, String value) {
            params.put(key, value);
            return this;
        }

        public Builder success(ISuccess success) {
            this.success = success;
            return this;
        }

        public Builder error(IError error) {
            this.error = error;
            return this;
        }

        public Builder request(IRequest request) {
            this.request = request;
            return this;
        }

        public Builder failure(IFailure failure) {
            this.failure = failure;
            return this;
        }

        public RestClient build() {
            return new RestClient(url, params, success, error, request, failure);
        }
    }

    private void request(HttpMethod method) {
        RestService service = RestCreator.getRestService();
        Call<String> call = null;
        if (iRequest != null) {
            iRequest.onRequestStart();
        }
        switch (method) {
            case GET:
                call = service.get(url, params);
                break;
            case POST:
                call = service.post(url, params);
                break;
            default:
                break;
        }
        if (call != null) {
            call.enqueue(new RequestCallback(iSuccess, iFailure, iRequest, iError));
        }
    }

    public void get() {
        request(HttpMethod.GET);
    }

    public final void post() {
        request(HttpMethod.POST);
    }
}
