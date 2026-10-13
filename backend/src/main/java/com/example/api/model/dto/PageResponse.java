package com.example.api.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Taokn
 * @description
 * @date 2026/2/5 15:12
 */
@Data
public class PageResponse<T> {
    private List<T> data;        // 当前页数据
    private Integer pageNum;     // 当前页码
    private Integer pageSize;    // 每页大小
    private Long total;          // 总记录数
    private Integer totalPages;  // 总页数
}
