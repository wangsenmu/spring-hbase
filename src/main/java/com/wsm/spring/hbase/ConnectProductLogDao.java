package com.wsm.spring.hbase;


import com.wsm.spring.hbase.shared.pojo.ConnectProductLog;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * HBase ttd_connect_product_log相关操作
 * Created by wangsm on 2017/8/23.
 */
@Component
public class ConnectProductLogDao extends AbstractHBaseBaseDao<ConnectProductLog> {

    public static final String TABLE_NAME = "Ttdconnectproduct_log";
    public static final String FAMILY_NAME = "cf";
    public static final String QUALIFIER_LOG = "log";

    @Override
    public String rowKeyBuild(ConnectProductLog productLog) {
        String rowKey = "";
        try {
            Long timestamp = System.currentTimeMillis();
            rowKey = String.format("%s_%s_%s_%s",
                    productLog.getConnectNo() == null ? "0" : productLog.getConnectNo(),
                    productLog.getVendorProductId() == null ? "0" : productLog.getVendorProductId(),
                    (Long.MAX_VALUE - timestamp), this.getRandom(1000, 9999));
        } catch (Exception ex) {
//            ClogManager.error(this.getClass().getSimpleName() + "->rowKeyBuild", ex);
        }
        return rowKey;
    }

    /**
     * 获取随机数
     *
     * @param min
     * @param max
     * @return
     */
    private int getRandom(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }

    @Override
    protected String initTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String initFamilyName() {
        return FAMILY_NAME;
    }


}
