package com.example.api.service.impl;

import com.example.api.model.entity.*;
import com.example.api.model.vo.CommodityChartVo;
import com.example.api.repository.CommodityRepository;
import com.example.api.repository.InventoryRecordRepository;
import com.example.api.repository.InventoryRepository;
import com.example.api.service.InventoryRecordService;
import com.example.api.utils.DataTimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InventoryRecordServiceImpl implements InventoryRecordService {

    @Resource
    private InventoryRepository inventoryRepository;

    @Resource
    private CommodityRepository commodityRepository;

    @Resource
    private InventoryRecordRepository recordRepository;

    @Override
    public List<CommodityChartVo> analyzeCommodity(Integer type) {
        List<CommodityChartVo> result = new ArrayList<>();
        List<InventoryRecord> all = recordRepository.findAllByType(type);
        Map<String, Integer> map = new HashMap<>();
        for (InventoryRecord r : all) {
            if (map.containsKey(r.getName())) {
                map.put(r.getName(), map.get(r.getName()) + r.getCount());
            } else {
                map.put(r.getName(), r.getCount());
            }
        }
        for (String key : map.keySet()) {
            result.add(new CommodityChartVo(map.get(key), key));
        }
        return result;
    }

    @Override
    public List<InventoryRecord> findAllByWarehouseId(String wid) {
        return recordRepository.findAllByWid(wid);
    }

    @Override
    public List<InventoryRecord> findAllByCommodityId(String cid) {

        List<InventoryRecord> list = recordRepository.findAllByCid(cid).stream()
                .sorted(Comparator.comparing(
                        InventoryRecord::getCreateAt,
                        Comparator.reverseOrder()
                ))
                .collect(Collectors.toList());

        // 只更新未读的
        List<InventoryRecord> unread = list.stream()
                .filter(r -> r.getType() == 0)
                .collect(Collectors.toList());

        unread.forEach(r -> r.setType(1));
        recordRepository.saveAll(unread);

        return list;
    }

    @Override
    public InventoryRecord out(InventoryRecord record) throws Exception {

        //查找当前商品在该仓库的库存
        Inventory inventory = inventoryRepository.findByWidAndCid(record.getWid(), record.getCid());
        //查询结果为空
        if (inventory == null) throw new Exception("仓库内不存在该商品");
        //比较库存
        if (inventory.getCount() < record.getCount()) throw new Exception("出库失败，库存数量不足");

        Optional<Commodity> optional = commodityRepository.findById(record.getCid());
        if (optional == null) {
            throw new Exception("不存在的商品id");
        }
        Commodity commodity = optional.get();
        commodity.setCount(commodity.getCount() - record.getCount());
        commodityRepository.save(optional.get());
        inventory.setCount(inventory.getCount() - record.getCount());

        inventoryRepository.save(inventory);
        record.setCreateAt(DataTimeUtil.getNowTimeString());
        record.setType(-1);
        return recordRepository.save(record);
    }

    @Override
    public InventoryRecord in(InventoryRecord record) throws Exception {
        Optional<Commodity> optional = commodityRepository.findById(record.getCid());
        if (optional == null) {
            throw new Exception("不存在的商品id");
        }
        Commodity commodity = optional.get();
        commodity.setCount(commodity.getCount() + record.getCount());
        commodityRepository.save(optional.get());

        //查找当前商品在该仓库的库存
        Inventory inventory = inventoryRepository.findByWidAndCid(record.getWid(), record.getCid());
        //查询结果为空
        if (inventory == null) {
            //新建该商品库存信息
            inventory = new Inventory();
            inventory.setCid(record.getCid());
            inventory.setWid(record.getWid());
            inventory.setCount(0);
            inventory.setName(record.getName());
        }
        inventory.setCount(inventory.getCount() + record.getCount());
        inventoryRepository.save(inventory);
        record.setCreateAt(DataTimeUtil.getNowTimeString());
        record.setType(+1);
        return recordRepository.save(record);
    }

    /**
     * 您的取件快递待取件，快递单号***已入库，由***投放至快递柜***柜口***地理位置***，取件码为***，请尽快去取
     * 您的寄件快递待取件，快递单号***已入库，由***投放至快递柜***柜口***地理位置***，取件码为***，请尽快去取
     * 您的收件快递状态更新啦，快递单号***由***出库
     * 您的寄件快递状态更新啦，快递单号***由***出库
     */
    @Override
    public void saveExpressLog(String noticeUserId, String expressNo, String username, Integer bizType, TCabinetBox tCabinetBox, TCabinet tCabinet, String code, Integer inOrOut) throws Exception {
        InventoryRecord inventoryRecord = new InventoryRecord();
        StringBuilder stringBuilder = new StringBuilder();
        if (bizType == 1) {
            stringBuilder.append("您的取件快递：").append(expressNo).append("，");
        } else if (bizType == 2) {
            stringBuilder.append("您的寄件快递：").append(expressNo).append("，");
        } else {
            throw new Exception("未知业务类型！");
        }
        if (inOrOut == 1) {
            stringBuilder.append("代取件，").append("由").append(username)
                    .append("投放至快递柜：").append(tCabinet.getCabinetName())
                    .append("，地理位置：").append(tCabinet.getLocation())
                    .append("，柜口：").append(tCabinetBox.getBoxNo())
                    .append("。取件码为：").append(code).append("，请尽快去取。");
        } else {
            stringBuilder.append("状态更新啦，").append("由").append(username).append("出库。");
        }
        inventoryRecord.setCreateAt(LocalDateTime.now().toString());
        inventoryRecord.setCid(noticeUserId);
        inventoryRecord.setDescription(stringBuilder.toString());
        inventoryRecord.setType(0);
        recordRepository.save(inventoryRecord);
    }

    @Override
    public void saveNotice(InventoryRecord inventoryRecord) {
        inventoryRecord.setCreateAt(DataTimeUtil.getNowTimeString());
        inventoryRecord.setCount(1);
        recordRepository.save(inventoryRecord);
    }

    @Override
    public List<InventoryRecord> findByCount(Integer count) {
        return recordRepository.findAllByCount(1);
    }

    @Override
    public void updateById(InventoryRecord inventoryRecord) {
        recordRepository.save(inventoryRecord);
    }

    @Override
    public void delete(String id) {
        recordRepository.deleteById(id);
    }

}
