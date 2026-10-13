package com.example.api.controller;

import com.example.api.model.dto.PageResponse;
import com.example.api.model.entity.TCabinet;
import com.example.api.model.entity.TExpress;
import com.example.api.model.entity.TExpressIn;
import com.example.api.model.entity.TExpressStock;
import com.example.api.service.CabinetService;
import com.example.api.service.ExpressService;
import com.example.api.service.ExpressStockService;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/express/stock")
public class ExpressStockController {

    @Autowired
    private ExpressStockService expressStockService;
    @Autowired
    private ExpressService expressService;

    @Autowired
    private CabinetService cabinetService;

    @GetMapping("/list")
    public PageResponse<TExpressStock> list(@RequestParam(required = false) String expressNo,
                                            @RequestParam(required = false) Integer status,
                                            @RequestParam(required = false) Integer pageNum,
                                            @RequestParam(required = false) Integer pageSize) {
        List<TExpressStock> all = expressStockService.findAll();
        Map<Long, TExpress> expressMap = expressService.findAll().stream().collect(Collectors.toMap(TExpress::getId, Function.identity()));
        Map<Long, TCabinet> cabinetMap = cabinetService.findAll().stream().collect(Collectors.toMap(TCabinet::getId, Function.identity()));
        for (TExpressStock tExpressStock : all) {
            TExpress tExpress = expressMap.get(tExpressStock.getExpressId());
            if (tExpress == null) {
                tExpressStock.setExpressNo("该快递单号已删除");
            } else {
                tExpressStock.setExpressNo(tExpress.getExpressNo());
            }
            TCabinet tCabinet = cabinetMap.get(tExpressStock.getCabinetId());
            if (tCabinet == null) {
                tExpressStock.setCabinetName("该快递柜已删除");
            } else {
                tExpressStock.setCabinetName(tCabinet.getCabinetName());
            }

        }
        if (expressNo != null && !expressNo.trim().equals("")) {
            all = all.stream()
                    .filter(e -> e.getExpressNo() != null && e.getExpressNo().contains(expressNo))
                    .collect(Collectors.toList());
        }
        if (status != null) {
            all = all.stream()
                    .filter(e -> e.getStatus() != null && e.getStatus().equals(status))
                    .collect(Collectors.toList());
        }
        // 3. 手动分页
        int total = all.size();
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);

        List<TExpressStock> pageData;
        if (startIndex > total) {
            pageData = new ArrayList<>();
        } else {
            pageData = all.subList(startIndex, endIndex);
        }

        PageResponse<TExpressStock> response = new PageResponse<>();
        response.setData(pageData);
        response.setPageNum(pageNum);
        response.setPageSize(pageSize);
        response.setTotal((long) total);
        response.setTotalPages((int) Math.ceil((double) total / pageSize));
        return response;
    }



    @GetMapping("/export")
    public void export(
            @RequestParam(required = false) String expressNo,
            @RequestParam(required = false) Integer status,
            HttpServletResponse response) throws IOException {

        // 1. 获取所有库存数据
        List<TExpressStock> all = expressStockService.findAll();

        // 2. 查询快递和柜子信息
        Map<Long, TExpress> expressMap = expressService.findAll()
                .stream()
                .collect(Collectors.toMap(TExpress::getId, Function.identity()));
        Map<Long, TCabinet> cabinetMap = cabinetService.findAll()
                .stream()
                .collect(Collectors.toMap(TCabinet::getId, Function.identity()));

        // 3. 填充快递号和柜名
        for (TExpressStock stock : all) {
            TExpress tExpress = expressMap.get(stock.getExpressId());
            stock.setExpressNo(tExpress == null ? "该快递单号已删除" : tExpress.getExpressNo());

            TCabinet tCabinet = cabinetMap.get(stock.getCabinetId());
            stock.setCabinetName(tCabinet == null ? "该快递柜已删除" : tCabinet.getCabinetName());
        }

        // 4. 按条件过滤
        if (expressNo != null && !expressNo.trim().isEmpty()) {
            all = all.stream()
                    .filter(e -> e.getExpressNo() != null && e.getExpressNo().contains(expressNo))
                    .collect(Collectors.toList());
        }
        if (status != null) {
            all = all.stream()
                    .filter(e -> e.getStatus() != null && e.getStatus().equals(status))
                    .collect(Collectors.toList());
        }

        // 5. 创建 Excel
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("快递库存");

        // 表头
        String[] headers = {"快递单号", "快递柜名", "柜口", "状态", "入库时间"};
        HSSFRow headRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headRow.createCell(i).setCellValue(headers[i]);
        }

        // 数据行
        int rowNum = 1;
        for (TExpressStock stock : all) {
            HSSFRow row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(stock.getExpressNo());
            row.createCell(1).setCellValue(stock.getCabinetName());
            row.createCell(2).setCellValue(stock.getBoxNo());
            row.createCell(3).setCellValue(stock.getStatus() == 0 ? "在库" : "已取");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            row.createCell(4).setCellValue(
                    stock.getInTime() != null ? stock.getInTime().format(formatter) : ""
            );
        }

        // 6. 输出 Excel
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=express_stock.xls");
        workbook.write(response.getOutputStream());
        workbook.close();
    }


}
