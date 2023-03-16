package com.example.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xpand.starter.canal.annotation.CanalEventListener;

import com.xpand.starter.canal.annotation.UpdateListenPoint;

import java.util.List;

@CanalEventListener
public class CanalDataEventListener {
    // 更新数据操作
    @UpdateListenPoint
    public void onEventUpdate(CanalEntry.RowData rowData){
        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
        for (CanalEntry.Column column : beforeColumnsList) {
            System.out.println(column.getName());
            System.out.println(column.getValue());
            System.out.println("==================================");
        }
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
        for (CanalEntry.Column column : afterColumnsList) {
            System.out.println(column.getName());
            System.out.println(column.getValue());
            System.out.println("----------------------------------");
        }
    }
}
