package com.wsm.spring.hbase;

import com.wsm.spring.hbase.shared.pojo.ConnectProductLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by wangsm on 2018/1/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
public class ConnectProductLogDaoTest {

    @Autowired
    private ConnectProductLogDao connectProductLogDao;

    @Test
    public void insertTest(){
        ConnectProductLog productLog = new ConnectProductLog();
        productLog.setConnectNo("10001");
        productLog.setLogType("1");
        productLog.setCreateUser("wsm");
        productLog.setMessage("testMessage");
        productLog.setRequestContent("requestCo");
        productLog.setVendorProductId("10001");
        connectProductLogDao.insertRow(productLog);
    }

    @Test
    public void getTest(){
       /* ConnectProductLog productLog = new ConnectProductLog();
        productLog.setConnectNo("10001");
        productLog.setLogType("1");
        productLog.setCreateUser("wsm");
        productLog.setMessage("testMessage");
        productLog.setRequestContent("requestCo");
        productLog.setVendorProductId("10001");*/

        List<ConnectProductLog> list =  connectProductLogDao.scanObjects("10001_10001",10);

        System.out.println(list);

    }



}
