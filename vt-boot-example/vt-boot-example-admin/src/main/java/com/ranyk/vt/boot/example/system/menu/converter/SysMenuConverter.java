package com.ranyk.vt.boot.example.system.menu.converter;

import com.ranyk.vt.boot.example.system.menu.domain.dto.MenuFormDTO;
import com.ranyk.vt.boot.example.system.menu.domain.entity.SysMenu;
import com.ranyk.vt.boot.example.system.menu.domain.po.MenuFormPO;
import com.ranyk.vt.boot.example.system.menu.domain.vo.MenuVO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * CLASS_NAME: SysMenuConverter.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 菜单实体与 DTO/VO 之间的转换器, 基于 MapStruct 在编译期生成实现
 * @date: 2026-11-20
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysMenuConverter {

    /**
     * 接口入参转业务表单 DTO
     *
     * @param formPO 菜单新增/修改接口入参
     * @return 菜单表单参数 {@link MenuFormDTO}
     */
    MenuFormDTO toFormDTO(MenuFormPO formPO);

    /**
     * 菜单实体转菜单视图对象, 下级菜单由业务层单独组装
     *
     * @param menu 菜单实体
     * @return 菜单视图对象 {@link MenuVO}
     */
    @Mapping(target = "children", ignore = true)
    MenuVO toVO(SysMenu menu);

    /**
     * 菜单表单转菜单实体(新增场景), 主键与审计字段由数据库自动填充
     *
     * @param formDTO 菜单表单参数
     * @return 菜单实体 {@link SysMenu}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    SysMenu toEntity(MenuFormDTO formDTO);

    /**
     * 将菜单表单的非空字段合并到已有菜单实体(修改场景); 审计字段不在此处覆盖
     *
     * @param formDTO 菜单表单参数
     * @param menu    待更新的菜单实体
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    void updateEntity(MenuFormDTO formDTO, @MappingTarget SysMenu menu);
}
