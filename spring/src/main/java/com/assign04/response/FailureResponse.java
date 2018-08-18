package com.assign04.response;

import com.assign04.response.HTTPResponse;

public class FailureResponse extends HTTPResponse {

    private String reason;

    public FailureResponse(String reason) {
        super("failed");
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

}
