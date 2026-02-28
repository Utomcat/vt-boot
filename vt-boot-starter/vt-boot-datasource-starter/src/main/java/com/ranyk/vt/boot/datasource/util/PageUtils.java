package com.ranyk.vt.boot.datasource.util;

import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ranyk.vt.boot.base.domain.dto.BaseDTO;
import com.ranyk.vt.boot.base.response.PageResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * CLASS_NAME: PageUtils.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 分页工具类
 * @date: 2026-02-28
 */
@Slf4j
public class PageUtils {

    /**
     * 默认当前页码
     */
    private static final long DEFAULT_CURRENT_PAGE = 1L;
    /**
     * 默认分页大小
     */
    private static final long DEFAULT_PAGE_SIZE = 10L;

    /**
     * 构建分页对象
     *
     * @param dto 分页参数对象
     * @return 分页对象
     */
    public static <T> Page<T> buildPage(BaseDTO dto) {
        // 判空处理
        if (dto == null) {
            return new Page<>(DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE);
        }
        // 获取页码和页面大小，处理空值和非法值
        long currentPage = parseOrDefault(dto.getCurrentPage(), DEFAULT_CURRENT_PAGE);
        long pageSize = parseOrDefault(dto.getPageSize(), DEFAULT_PAGE_SIZE);
        return new Page<>(currentPage, pageSize);
    }

    /**
     * 将输入值解析为 Long 类型，若解析失败或为 null，则返回默认值
     *
     * @param value        输入值
     * @param defaultValue 默认值
     */
    private static long parseOrDefault(Object value, long defaultValue) {
        if (Objects.isNull(value)) {
            return defaultValue;
        }
        try {
            return Long.parseLong(String.valueOf(value));
        } catch (NumberFormatException e) {
            // 记录警告日志（可根据实际需求调整）
            log.error("Invalid numeric value: {}, using default: {}", value, defaultValue);
            return defaultValue;
        }
    }

    /**
     * 构建分页响应对象
     *
     * @param list  分页列表
     * @param data  分页数据
     * @param <T>   泛型 T 返回结果泛型
     * @param <B>   泛型 B 分页列表泛型 - 数据表映射实体类
     * @return 分页响应对象 {@link PageResponse}
     */
    public static <T,B> PageResponse<T> buildPageResponse(IPage<B> list, List<T> data){
        return PageResponse.<T>builder()
                .code(String.valueOf(HttpStatus.HTTP_OK))
                .currentPage(Integer.parseInt(String.valueOf(list.getCurrent())))
                .pageSize(Integer.parseInt(String.valueOf(list.getSize())))
                .total(Integer.parseInt(String.valueOf(list.getTotal())))
                .totalPage(Integer.parseInt(String.valueOf(list.getPages())))
                .data(data)
                .build();

    }
}
