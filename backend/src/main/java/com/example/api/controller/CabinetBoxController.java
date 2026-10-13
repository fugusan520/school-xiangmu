package com.example.api.controller;
import com.example.api.model.dto.CabinetBoxDTO;
import com.example.api.model.entity.TCabinet;
import com.example.api.model.entity.TCabinetBox;
import com.example.api.service.CabinetBoxService;
import com.example.api.service.CabinetService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cabinet/box")
public class CabinetBoxController {

    @Autowired
    private CabinetBoxService cabinetBoxService;

    @Autowired
    private CabinetService cabinetService;

    /** 柜口列表（按柜子） */
    @GetMapping("/list")
    public List<TCabinetBox> list(@RequestParam(required = false) Long cabinetId) {
        if (cabinetId == null) {
            List<TCabinetBox> all = cabinetBoxService.findAll();
            if (CollectionUtils.isEmpty(all)) {
                return new ArrayList<>();
            }
            List<TCabinet> cabinets = cabinetService.findByIds(all.stream().map(TCabinetBox::getCabinetId).distinct().collect(Collectors.toList()));
            Map<Long, TCabinet> cabinetMap = cabinets.stream().collect(Collectors.toMap(TCabinet::getId, Function.identity()));

            for (TCabinetBox tCabinetBox : all) {
                tCabinetBox.setCabinetName(cabinetMap.get(tCabinetBox.getCabinetId()).getCabinetName());
            }
            return all;
        }
        return cabinetBoxService.findByCabinetId(cabinetId);
    }

    /** 新增柜口 */
    @PostMapping("/save")
    public TCabinetBox save(@RequestBody TCabinetBox box) {
        if (box.getCabinetId() == null) {
            throw new RuntimeException("所属快递柜不能为空");
        }
        if (box.getBoxNo() == null || box.getBoxNo().trim().isEmpty()) {
            throw new RuntimeException("柜口编号不能为空");
        }
        if (box.getBoxType() == null) {
            throw new RuntimeException("柜口类型不能为空");
        }
        box.setStatus(0); // 默认空闲
        return cabinetBoxService.save(box);
    }

    @PostMapping("/update")
    public void update(@RequestBody TCabinetBox box) {
        cabinetBoxService.update(box);
    }

    /** 修改状态 */
    @PutMapping("/status")
    public void changeStatus(@RequestParam Long id,
                             @RequestParam Integer status) throws Exception {
        TCabinetBox box = cabinetBoxService.findById(id);

        if (box == null) {
            throw new RuntimeException("柜口不存在");
        }
        if (box.getStatus() == 1) {
            throw new RuntimeException("占用中的柜口不能修改状态");
        }
        box.setStatus(status);
        cabinetBoxService.update(box);
    }

    /** 删除柜口 */
    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) throws Exception {
        TCabinetBox box = cabinetBoxService.findById(id);

        if (box == null) {
            throw new RuntimeException("柜口不存在");
        }
        if (box.getStatus() == 1) {
            throw new RuntimeException("占用中的柜口不能删除");
        }
        cabinetBoxService.delete(id);
    }


    @PostMapping("/batchSave")
    public void batchSave(@RequestBody List<TCabinetBox> list) {
        for (TCabinetBox tCabinetBox : list) {
            if (tCabinetBox.getCabinetId() == null) {
                throw new RuntimeException("所属快递柜不能为空");
            }
            if (tCabinetBox.getBoxNo() == null || tCabinetBox.getBoxNo().trim().isEmpty()) {
                throw new RuntimeException("柜口编号不能为空");
            }
            if (tCabinetBox.getBoxType() == null) {
                throw new RuntimeException("柜口类型不能为空");
            }
            tCabinetBox.setStatus(0);
        }

        cabinetBoxService.saveAll(list);
    }

    @GetMapping("/canUseList")
    public List<CabinetBoxDTO> canUseList() throws Exception {
        List<TCabinet> all = cabinetService.findAll();
        List<TCabinet> canUseCabinet = all.stream().filter(a -> a.getStatus().equals(0)).collect(Collectors.toList());
        List<CabinetBoxDTO> list = Lists.newArrayList();
        if (CollectionUtils.isEmpty(canUseCabinet)) {
            throw new Exception("暂无可用的快递柜");
        }

        for (TCabinet tCabinet : canUseCabinet) {
            List<TCabinetBox> byCabinetId = cabinetBoxService.findByCabinetId(tCabinet.getId());
            List<TCabinetBox> collect = byCabinetId.stream().filter(a -> a.getStatus().equals(0)).collect(Collectors.toList());
            CabinetBoxDTO cabinetBoxDTO = new CabinetBoxDTO();
            cabinetBoxDTO.setCabinetId(tCabinet.getId());
            cabinetBoxDTO.setCabinetName(tCabinet.getCabinetName());
            cabinetBoxDTO.setLocation(tCabinet.getLocation());
            cabinetBoxDTO.setBoxes(collect);
            list.add(cabinetBoxDTO);
        }

        return list;
    }

}
