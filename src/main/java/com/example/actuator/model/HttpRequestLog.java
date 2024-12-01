package com.example.actuator.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpRequestLog {
    /* request */
    private String method;
    private String uri;
    private int status;
    /* header 분리할것 */
    private String host;
    private String userAgent;
    private String accept;
    private long timestamp;

    /*response 분리할것(요청과 중복 분리) */
    private String contentType;
    private String contentLength;
}
