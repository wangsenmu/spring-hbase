package com.wsm.spring.hbase;

import com.wsm.spring.hbase.shared.util.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangsm on 2017/11/24.
 */
public abstract class AbstractHBaseBaseDao<T> {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    private Class<T> cls;

    protected abstract String initTableName();

    protected abstract String initFamilyName();

    protected abstract String rowKeyBuild(T t);

    public AbstractHBaseBaseDao(){
        //取得子类的类型
        Type t = getClass().getGenericSuperclass();
        ParameterizedType p = (ParameterizedType) t ;
        cls = (Class<T>) p.getActualTypeArguments()[0];
    }

    public void updateRow(String rowKey, T bean) {
        if (StringUtils.isBlank(rowKey))
            return;
        hbaseTemplate.setAutoFlush(false); // 关闭自动提交，批量提交，降低系统开销
        this.addOrUpdate(hbaseTemplate, rowKey, bean);
        hbaseTemplate.setAutoFlush(true); // 开启自动提交，尽快提交当前批次
    }

    /**
     * 通过条件检索获取符合条件的rowKey list
     *
     * @param rowPrefix
     * @param limit
     * @param startRow
     * @param stopRow
     * @param minStamp
     * @param maxStamp
     * @return
     */
    public List<String> scanRowKeys(String rowPrefix, Integer limit, String startRow, String stopRow, Long minStamp, Long maxStamp) {
        List<String> rows = null;
        if (StringUtils.isBlank(rowPrefix))
            return rows;
        FilterList filterList = new FilterList();
        Scan scan = this.getScan(rowPrefix, limit, filterList, startRow, stopRow, minStamp, maxStamp);
        scan.setFilter(filterList);
//        scan.setRowPrefixFilter(rowPrefix.getBytes()); // 前缀匹配查询
        rows = hbaseTemplate.find(this.initTableName(), scan, new RowMapper<String>() {
            @Override
            public String mapRow(Result result, int rowNum) throws Exception {
                List<Cell> cellList = result.listCells();
                if (cellList != null && !cellList.isEmpty())
                    return new String(CellUtil.cloneRow(cellList.get(0)));
                return null;
            }
        });
        return rows;
    }

    /**
     * 通过条件扫描返回符合条件的 cls 对象结果集
     *
     * @param rowPrefix
     * @param limit
     * @param startRow
     * @param stopRow
     * @param minStamp
     * @param maxStamp
     * @return
     */
    public List<T> scanObjects(String rowPrefix, Integer limit, String startRow, String stopRow, Long minStamp, Long maxStamp) {
        List<T> tList = new ArrayList<>();
        if (StringUtils.isBlank(rowPrefix))
            return tList;
        FilterList filterList = new FilterList();
        final Scan scan = this.getScan(rowPrefix, limit, filterList, startRow, stopRow, minStamp, maxStamp);
        scan.setFilter(filterList);
//        scan.setRowPrefixFilter(rowPrefix.getBytes());
        tList = this.scanList(scan);
        return tList;
    }

    /**
     * 根据RowKey 前缀扫描对象
     *
     * @param rowPrefix 前缀匹配的row
     * @param limit     一次最多取的数量
     * @return
     */
    public List<T> scanObjects(String rowPrefix, Integer limit) {
        List<T> tList = new ArrayList<>();
        if (StringUtils.isBlank(rowPrefix))
            return tList;
        FilterList filterList = new FilterList();
        final Scan scan = this.getScan(rowPrefix, limit, filterList, null, null, null, null);
        scan.setFilter(filterList);
//        scan.setRowPrefixFilter(rowPrefix.getBytes());
        tList = this.scanList(scan);
        return tList;
    }

    /*
     * 根据Rowkey查找
     */
    public T queryByRowKey(String rowKey) {
        String rowString = hbaseTemplate.get(this.initTableName(), rowKey, this.initFamilyName(), new RowMapper<String>() {
            @Override
            public String mapRow(Result result, int rowNum) throws Exception {
                Map<String, String> cellMap = new HashMap<String, String>();
                List<Cell> cellList = result.listCells();
                for (Cell cell : cellList) {
                    String qualifier = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                    String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                    cellMap.put(qualifier, value);
                }
                return JSONUtil.toJson(cellMap);
            }

        });
        return JSONUtil.fromJson(rowString, cls);
    }


    /*
     * 移除指定rowkey行
     */
    public void delete(String rowKey) {
        hbaseTemplate.delete(this.initTableName(), rowKey, this.initFamilyName());
    }

    /*
   * 移除多行rowkey
   */
    public void batchDelete(List<String> rowKeys) {
        hbaseTemplate.setAutoFlush(false); // 关闭自动提交，批量提交，降低系统开销
        for (String rowKey : rowKeys) {
            this.delete(rowKey);
        }
        hbaseTemplate.setAutoFlush(true); // 开启自动提交，尽快提交当前批次
    }

    /*
     * 批量写入行
     */
    public void insertBatchRow(Map<String, T> beans) {
        hbaseTemplate.setAutoFlush(false); // 关闭自动提交，批量提交，降低系统开销
        for (String rowName : beans.keySet()) {
            T bean = beans.get(rowName);
            this.addOrUpdate(hbaseTemplate, rowKeyBuild(bean), bean);
        }
        hbaseTemplate.setAutoFlush(true); // 开启自动提交，尽快提交当前批次
    }

    /*
     * 写入行
     */
    public void insertRow(T bean) {
        insertRow(null, bean);
    }

    /*
 * 写入行
 */
    public void insertRow(String rowKey, T bean) {

        if (StringUtils.isBlank(rowKey))
            rowKey = this.rowKeyBuild(bean);
        if (StringUtils.isBlank(rowKey))
            return;
        hbaseTemplate.setAutoFlush(false); // 关闭自动提交，批量提交，降低系统开销
        this.addOrUpdate(hbaseTemplate, rowKey, bean);
        hbaseTemplate.setAutoFlush(true); // 开启自动提交，尽快提交当前批次
    }

    /**
     * 格式化 HbaseTemplate 将 bean 中对象属性和内容提交
     *
     * @param hbaseTemplate
     * @param rowKey
     * @param bean
     * @return
     */
    private HbaseTemplate addOrUpdate(HbaseTemplate hbaseTemplate, String rowKey, T bean) {
        if (hbaseTemplate == null)
            return hbaseTemplate;
        Map<String, Object> attributes = JSONUtil.formatJsonToMap(JSONUtil.toJson(bean));
        for (String qualifier : attributes.keySet()) {
            byte[] valueByte = null;
            Object value = attributes.get(qualifier);
            if (value != null) {
                valueByte = value.toString().getBytes();
            }
            hbaseTemplate.put(this.initTableName(), rowKey, this.initFamilyName(), qualifier, valueByte);
        }
        return hbaseTemplate;
    }

    /**
     * 抽离公共部分代码 通过scan 和cls 返回序列化以后的结果
     *
     * @param scan
     * @return
     */
    private List<T> scanList(Scan scan) {
        List<T> tList = new ArrayList<>();
        List<String> rows = hbaseTemplate.find(this.initTableName(), scan, new RowMapper<String>() {
            @Override
            public String mapRow(Result result, int rowNum) throws Exception {
                Map<String, String> cellMap = new HashMap<>();
                List<Cell> cellList = result.listCells();
                for (Cell cell : cellList) {
                    String qualifier = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                    String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                    cellMap.put(qualifier, value);
                }
                return JSONUtil.toJson(cellMap);
            }
        });
        for (String row : rows) {
            tList.add(JSONUtil.fromJson(row, cls));
        }
        return tList;
    }

    /**
     * 格式化需要设置字段的scan
     *
     * @param limit    扫描条数
     * @param startRow 开始rowkey
     * @param stopRow  结束rowkey
     * @param minStamp 最小时间戳
     * @param maxStamp 最大时间戳
     * @return
     */
    private Scan getScan(String rowPrefix, Integer limit, FilterList filterList, String startRow, String stopRow, Long minStamp, Long maxStamp) {
        Scan scan = new Scan();
        if (StringUtils.isNotBlank(startRow))
            scan.setStartRow(Bytes.toBytes(startRow));
        if (StringUtils.isNotBlank(stopRow))
            scan.setStopRow(Bytes.toBytes(stopRow));
       /* //设置时间范围
        *//*if (minStamp != null && minStamp > 0 && maxStamp != null && maxStamp > 0) {
            try {
                scan.setTimeRange(minStamp, maxStamp);
            } catch (IOException e) {
                ClogManager.error("AbstractHBaseBaseDao-> getScan() :最大时间最小时间范围或者格式设置不正确", e);
            }
        }*/
        if (filterList == null)
            filterList = new FilterList();
        if (minStamp != null && minStamp > 0) {
            Filter createTimeGreater = new SingleColumnValueFilter(Bytes.toBytes(this.initFamilyName()), Bytes.toBytes("createTime"), CompareFilter.CompareOp.GREATER_OR_EQUAL, Bytes.toBytes(String.valueOf(minStamp)));
            filterList.addFilter(createTimeGreater);
        }
        if (maxStamp != null && maxStamp > 0) {
            Filter createTimeLess = new SingleColumnValueFilter(Bytes.toBytes(this.initFamilyName()), Bytes.toBytes("createTime"), CompareFilter.CompareOp.LESS_OR_EQUAL, Bytes.toBytes(String.valueOf(maxStamp)));
            filterList.addFilter(createTimeLess);
        }
        PrefixFilter prefixFilter = new PrefixFilter(rowPrefix.getBytes());
        filterList.addFilter(prefixFilter);
        if (limit == null || limit == 0) //默认100条
            filterList.addFilter(new PageFilter(100));
        else
            filterList.addFilter(new PageFilter(limit));

        return scan;
    }
}
