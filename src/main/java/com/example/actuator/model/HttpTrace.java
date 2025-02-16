package com.example.actuator.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "HTTP_TRACE")
public class HttpTrace {

    @Id
    @Column(name = "HTTP_ID", nullable = false, length = 36)
    private String httpId;

    @Column(name = "HTTP_URI", nullable = false, length = 255)
    private String httpUri;

    @Column(name = "HTTP_METHOD", nullable = false, length = 10)
    private String httpMethod;

    @Column(name = "HTTP_REQUEST_DATE", nullable = false)
    private LocalDateTime httpRequestDate;

    @Column(name = "HTTP_REQUEST_HOST", nullable = false, length = 255)
    private String httpRequestHost;

    @Column(name = "HTTP_REQUEST_HEADER_CONTENT_TYPE", length = 255)
    private String httpRequestHeaderContentType;

    @Column(name = "HTTP_REQUEST_HEADER_ACCEPT", length = 255)
    private String httpRequestHeaderAccept;

    @Column(name = "HTTP_REQUEST_AUTHORIZATION", length = 255)
    private String httpRequestAuthorization;

    //@Lob
    //@Column(name = "HTTP_REQUEST_BODY")
    //private String httpRequestBody;

    @Column(name = "HTTP_RESPONSE_STATUS", length = 20)
    private String httpResponseStatus;

    @Column(name = "HTTP_RESPONSE_DATE", nullable = false)
    private LocalDateTime httpResponseDate;

    //@Lob
    //@Column(name = "HTTP_RESPONSE_BODY")
    //private String httpResponseBody;

    // Constructors

    public HttpTrace() {
        this.httpId = UUID.randomUUID().toString();
    }

    // Getters and Setters

    public String getHttpId() {
        return httpId;
    }

    public void setHttpId(String httpId) {
        this.httpId = httpId;
    }

    public String getHttpUri() {
        return httpUri;
    }

    public void setHttpUri(String httpUri) {
        this.httpUri = httpUri;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public LocalDateTime getHttpRequestDate() {
        return httpRequestDate;
    }

    public void setHttpRequestDate(LocalDateTime httpRequestDate) {
        this.httpRequestDate = httpRequestDate;
    }

    public String getHttpRequestHost() {
        return httpRequestHost;
    }

    public void setHttpRequestHost(String httpRequestHost) {
        this.httpRequestHost = httpRequestHost;
    }

    public String getHttpRequestHeaderContentType() {
        return httpRequestHeaderContentType;
    }

    public void setHttpRequestHeaderContentType(String httpRequestHeaderContentType) {
        this.httpRequestHeaderContentType = httpRequestHeaderContentType;
    }

    public String getHttpRequestHeaderAccept() {
        return httpRequestHeaderAccept;
    }

    public void setHttpRequestHeaderAccept(String httpRequestHeaderAccept) {
        this.httpRequestHeaderAccept = httpRequestHeaderAccept;
    }

    public String getHttpRequestAuthorization() {
        return httpRequestAuthorization;
    }

    public void setHttpRequestAuthorization(String httpRequestAuthorization) {
        this.httpRequestAuthorization = httpRequestAuthorization;
    }

//    public String getHttpRequestBody() {
//        return httpRequestBody;
//    }
//
//    public void setHttpRequestBody(String httpRequestBody) {
//        this.httpRequestBody = httpRequestBody;
//    }

    public String getHttpResponseStatus() {
        return httpResponseStatus;
    }

    public void setHttpResponseStatus(String httpResponseStatus) {
        this.httpResponseStatus = httpResponseStatus;
    }

    public LocalDateTime getHttpResponseDate() {
        return httpResponseDate;
    }

    public void setHttpResponseDate(LocalDateTime httpResponseDate) {
        this.httpResponseDate = httpResponseDate;
    }

//    public String getHttpResponseBody() {
//        return httpResponseBody;
//    }
//
//    public void setHttpResponseBody(String httpResponseBody) {
//        this.httpResponseBody = httpResponseBody;
//    }
}
