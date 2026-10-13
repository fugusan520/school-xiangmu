package com.example.api.service.impl;

import com.example.api.model.entity.TCabinet;
import com.example.api.model.entity.TCabinetBox;
import com.example.api.repository.TCabinetBoxRepository;
import com.example.api.service.CabinetBoxService;
import com.example.api.service.CabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Service
public class CabinetBoxServiceImpl implements CabinetBoxService {

    @Autowired
    private TCabinetBoxRepository cabinetBoxRepository;

    @Autowired
    private CabinetService cabinetService;

    @Override
    public List<TCabinetBox> findAll() {
        return cabinetBoxRepository.findAll();
    }

    @Override
    public List<TCabinetBox> findByCabinetId(Long cabinetId) {
        return cabinetBoxRepository.findByCabinetId(cabinetId);
    }

    @Override
    public TCabinetBox findById(Long id) throws Exception {
        return cabinetBoxRepository.findById(id).orElseThrow(() -> new Exception("未找到该Id的数据"));
    }

    @Override
    public void delete(Long id) {
        cabinetBoxRepository.deleteById(id);
    }

    @Override
    public void deleteAll(List<Long> ids) {
        cabinetBoxRepository.deleteAllById(ids);
    }

    @Override
    public void update(TCabinetBox box) {
        cabinetBoxRepository.save(box);
    }

    @Override
    public TCabinetBox save(TCabinetBox box) {
        checkTotalBoxes(1, box.getCabinetId());
        return cabinetBoxRepository.save(box);
    }

    @Override
    public void saveAll(List<TCabinetBox> list) {
        checkTotalBoxes(list.size(), list.get(0).getCabinetId());
        cabinetBoxRepository.saveAll(list);
    }

    @Override
    public List<TCabinetBox> findByCabinetIds(List<Long> cabinetIds) {
        return cabinetBoxRepository.findByCabinetIdIn(cabinetIds);
    }

    private void checkTotalBoxes(Integer newCount, Long cabinetId) {
        List<TCabinetBox> boxes = cabinetBoxRepository.findByCabinetId(cabinetId);
        TCabinet cabinet = cabinetService.findById(cabinetId);
        if (boxes.size() + newCount > cabinet.getTotalBoxes()) {
            throw new RuntimeException("格数不能大于快递柜总格数！");
        }
    }

}
