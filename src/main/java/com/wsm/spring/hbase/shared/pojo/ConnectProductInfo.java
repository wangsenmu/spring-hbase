package com.wsm.spring.hbase.shared.pojo;

/**
 * Created by sxu on 2017-11-22.
 */

public class ConnectProductInfo {

    private String vendorProductId;

    private String connectNo;

//    PRODUCT：产品对象
//    INVENTORY：库存对接对象
//    IMAGE：产品图片
    private String contentType;

    private String remark;

    private Long version;

    private String content;

    private String dataChange_CreateUser;

    private String dataChange_CreateTime;

    private String dataChange_LastUser;

    private String dataChange_LastTime;

    public String rowKey;

    public String getConnectNo() {
        return connectNo;
    }

    public void setConnectNo(String connectNo) {
        this.connectNo = connectNo;
    }

    public String getVendorProductId() {
        return vendorProductId;
    }

    public void setVendorProductId(String vendorProductId) {
        this.vendorProductId = vendorProductId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDataChange_CreateUser() {
        return dataChange_CreateUser;
    }

    public void setDataChange_CreateUser(String dataChange_CreateUser) {
        this.dataChange_CreateUser = dataChange_CreateUser;
    }

    public String getDataChange_CreateTime() {
        return dataChange_CreateTime;
    }

    public void setDataChange_CreateTime(String dataChange_CreateTime) {
        this.dataChange_CreateTime = dataChange_CreateTime;
    }

    public String getDataChange_LastUser() {
        return dataChange_LastUser;
    }

    public void setDataChange_LastUser(String dataChange_LastUser) {
        this.dataChange_LastUser = dataChange_LastUser;
    }

    public String getDataChange_LastTime() {
        return dataChange_LastTime;
    }

    public void setDataChange_LastTime(String dataChange_LastTime) {
        this.dataChange_LastTime = dataChange_LastTime;
    }

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(final String rowKey) {
        this.rowKey = rowKey;
    }
}
