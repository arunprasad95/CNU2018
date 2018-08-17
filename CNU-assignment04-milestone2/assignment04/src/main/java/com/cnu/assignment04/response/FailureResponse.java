package com.cnu.assignment04.response;

import com.cnu.assignment04.response.HTTPResponse;

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
