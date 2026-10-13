package com.example.api.controller;
import com.example.api.model.entity.TCabinet;
import com.example.api.model.entity.TCabinetBox;
import com.example.api.model.entity.TExpressIn;
import com.example.api.service.CabinetBoxService;
import com.example.api.service.CabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cabinet")
public class CabinetController {

    @Autowired
    private CabinetService cabinetService;

    @Autowired
    private CabinetBoxService cabinetBoxService;

    @GetMapping("/list")
    public List<TCabinet> list() {
        return cabinetService.findAll();
    }

    @PostMapping("/save")
    public TCabinet save(@RequestBody TCabinet in) {
        if (in.getCabinetNo() == null || in.getCabinetNo().trim().isEmpty()) {
            throw new IllegalArgumentException("柜号不能为空");
        }
        if (in.getCabinetName() == null || in.getCabinetName().trim().isEmpty()) {
            throw new IllegalArgumentException("柜子名称不能为空");
        }
        if (in.getLocation() == null || in.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("位置不能为空");
        }
        if (in.getTotalBoxes() == null) {
            throw new IllegalArgumentException("总格数不能为空");
        }
        in.setCreateTime(LocalDateTime.now());
        return cabinetService.save(in);
    }

    @DeleteMapping()
    public void delete(@RequestParam String id) throws Exception {
        cabinetService.deleteById(Long.valueOf(id));

        List<TCabinetBox> boxes = cabinetBoxService.findByCabinetId(Long.valueOf(id));
        if (!CollectionUtils.isEmpty(boxes)) {
            if (boxes.stream().anyMatch(b -> b.getStatus().equals(1))) {
                throw new Exception("该快递柜有柜口被占用，不可删除");
            }
            cabinetBoxService.deleteAll(boxes.stream().map(TCabinetBox::getId).collect(Collectors.toList()));
        }
    }

    @PostMapping("/update")
    public TCabinet update(@RequestBody TCabinet in) {
        return cabinetService.save(in);
    }


}
