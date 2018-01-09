package com.wsm.spring.hbase;


import com.wsm.spring.hbase.shared.pojo.ConnectProductInfo;
import org.springframework.stereotype.Component;

/**
 * Created by sxu on 2017-11-22.
 * HBase Ttdconnectproduct_ProductInfo相关操作
 */
@Component
public class ConnectProductInfoDao extends AbstractHBaseBaseDao<ConnectProductInfo> {
    public static final String TABLE_NAME = "Ttdconnectproduct_ProductInfo";
    public static final String CF = "cf";

    @Override
    protected String initTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String initFamilyName() {
        return CF;
    }

    @Override
    protected String rowKeyBuild(ConnectProductInfo connectProductInfo) {
        String rowKey = "";
        try {
            if (connectProductInfo == null)
                return rowKey;
            rowKey = String.format("%s_%s_%s_%s",
                    connectProductInfo.getConnectNo() == null ? "0" : connectProductInfo.getConnectNo(),
                    connectProductInfo.getContentType() == null ? "0" : connectProductInfo.getContentType(),
                    connectProductInfo.getVendorProductId() == null ? "0" : connectProductInfo.getVendorProductId(),
                    connectProductInfo.getVersion() == null ? "0" :  connectProductInfo.getVersion());
        } catch (Exception ex) {
//            ClogManager.error(this.getClass().getSimpleName() + "->rowKeyBuild", ex);
        }
        return rowKey;
    }


}
