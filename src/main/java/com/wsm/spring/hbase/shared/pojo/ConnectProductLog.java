package com.wsm.spring.hbase.shared.pojo;

public class ConnectProductLog {
    private String connectNo;

    private String vendorProductId;

    private String version;

    private String responseContent;

    private String sendURL;

    private String requestContent;

    private String message;

    private String logType;

    private Long createTime;

    private Integer status;

    private String createUser;

    private String rowKey;


    public String getConnectNo() {
        return connectNo;
    }

    public ConnectProductLog setConnectNo(String connectNo) {
        this.connectNo = connectNo;
        return this;
    }

    public String getVendorProductId() {
        return vendorProductId;
    }

    public ConnectProductLog setVendorProductId(String vendorProductId) {
        this.vendorProductId = vendorProductId;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public ConnectProductLog setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public ConnectProductLog setResponseContent(String responseContent) {
        this.responseContent = responseContent;
        return this;
    }

    public String getSendURL() {
        return sendURL;
    }

    public ConnectProductLog setSendURL(String sendURL) {
        this.sendURL = sendURL;
        return this;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public ConnectProductLog setRequestContent(String requestContent) {
        this.requestContent = requestContent;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ConnectProductLog setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getLogType() {
        return logType;
    }

    public ConnectProductLog setLogType(String logType) {
        this.logType = logType;
        return this;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public ConnectProductLog setCreateTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public ConnectProductLog setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public ConnectProductLog setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getRowKey() {
        return rowKey;
    }

    public ConnectProductLog setRowKey(String rowKey) {
        this.rowKey = rowKey;
        return this;
    }
}
