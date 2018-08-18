package com.assign04.response;

import com.assign04.response.HTTPResponse;

public class SuccessResponse extends HTTPResponse {

    private Object data;

    public Object getData() {
        return data;
    }

    public SuccessResponse(Object data) {
        super("success");
        this.data = data;
    }
}
