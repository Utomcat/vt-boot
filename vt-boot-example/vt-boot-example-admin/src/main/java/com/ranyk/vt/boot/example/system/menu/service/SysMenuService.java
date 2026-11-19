package com.ranyk.vt.boot.example.system.menu.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.example.constant.MenuTypeEnum;
import com.ranyk.vt.boot.example.constant.RbacConstant;
import com.ranyk.vt.boot.example.system.menu.converter.SysMenuConverter;
import com.ranyk.vt.boot.example.system.menu.domain.dto.MenuFormDTO;
import com.ranyk.vt.boot.example.system.menu.domain.entity.SysMenu;
import com.ranyk.vt.boot.example.system.menu.domain.vo.MenuVO;
import com.ranyk.vt.boot.example.system.menu.mapper.SysMenuMapper;
import com.ranyk.vt.boot.example.system.menu.util.MenuTreeUtils;
import com.ranyk.vt.boot.example.system.rbac.service.RbacService;
import com.ranyk.vt.boot.example.system.role.domain.entity.SysRoleMenu;
import com.ranyk.vt.boot.example.system.role.mapper.SysRoleMenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * CLASS_NAME: SysMenuService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 菜单与权限管理服务, 提供菜单树的查询、增删改能力
 * @date: 2026-11-20
 */
@Service
@RequiredArgsConstructor
public class SysMenuService {

    /**
     * 菜单数据访问对象
     */
    private final SysMenuMapper sysMenuMapper;
    /**
     * 角色-菜单关联数据访问对象
     */
    private final SysRoleMenuMapper sysRoleMenuMapper;
    /**
     * 菜单入参转换器
     */
    private final SysMenuConverter sysMenuConverter;
    /**
     * 权限解析服务, 用于在菜单变更后失效权限缓存
     */
    private final RbacService rbacService;

    /**
     * 查询全部菜单树(含目录、页面与按钮), 按排序值升序排列, 供菜单管理使用
     *
     * @return 树形菜单列表
     */
    public List<MenuVO> tree() {
        return MenuTreeUtils.build(toVOList(selectAllSorted()), RbacConstant.ROOT_PARENT_ID);
    }

    /**
     * 新增菜单。会校验名称与类型必填、类型合法、父菜单存在且层级关系合理,
     * 未指定的排序值为 0、状态为正常; 按钮类型默认不在侧边栏显示。
     *
     * @param formDTO 菜单表单参数
     * @return 新增菜单的ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Long create(MenuFormDTO formDTO) {
        validateBase(formDTO);
        long parentId = ObjectUtil.isNull(formDTO.getParentId()) ? RbacConstant.ROOT_PARENT_ID : formDTO.getParentId();
        SysMenu parent = getParentIfNecessary(parentId);
        validateHierarchy(formDTO.getType(), parent);
        SysMenu menu = sysMenuConverter.toEntity(formDTO);
        menu.setParentId(parentId);
        menu.setSort(ObjectUtil.isNull(formDTO.getSort()) ? 0 : formDTO.getSort());
        menu.setStatus(ObjectUtil.isNull(formDTO.getStatus()) ? RbacConstant.STATUS_NORMAL : formDTO.getStatus());
        if (ObjectUtil.isNull(formDTO.getVisible())) {
            menu.setVisible(MenuTypeEnum.BUTTON.getValue().equals(formDTO.getType())
                    ? 0 : RbacConstant.VISIBLE_YES);
        }
        sysMenuMapper.insert(menu);
        rbacService.evictAll();
        return menu.getId();
    }

    /**
     * 修改菜单。会校验菜单存在、父菜单不能是自身或自身的下级, 仅更新提交的非空字段
     *
     * @param formDTO 菜单表单参数, 必须携带菜单ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(MenuFormDTO formDTO) {
        if (ObjectUtil.isNull(formDTO.getId())) {
            throw new ServiceException("菜单ID不能为空");
        }
        SysMenu menu = getExistMenu(formDTO.getId());
        if (StrUtil.isNotBlank(formDTO.getType())) {
            validateType(formDTO.getType());
            SysMenu parent = ObjectUtil.isNull(formDTO.getParentId()) ? null : getParentIfNecessary(formDTO.getParentId());
            validateHierarchy(formDTO.getType(), parent);
        }
        if (ObjectUtil.isNotNull(formDTO.getParentId())) {
            if (ObjectUtil.equal(formDTO.getParentId(), menu.getId())) {
                throw new ServiceException("上级菜单不能是菜单自身");
            }
            if (isDescendant(menu.getId(), formDTO.getParentId())) {
                throw new ServiceException("上级菜单不能是当前菜单的下级");
            }
        }
        sysMenuConverter.updateEntity(formDTO, menu);
        sysMenuMapper.updateById(menu);
        rbacService.evictAll();
    }

    /**
     * 逻辑删除菜单。存在下级菜单时不允许删除; 删除后同步清理角色与该菜单的授权关系
     *
     * @param id 菜单ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        getExistMenu(id);
        Long childCount = sysMenuMapper.selectCount(
                Wrappers.<SysMenu>lambdaQuery().eq(SysMenu::getParentId, id));
        if (childCount > 0) {
            throw new ServiceException("存在下级菜单, 请先删除下级菜单");
        }
        sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getMenuId, id));
        sysMenuMapper.deleteById(id);
        rbacService.evictAll();
    }

    /**
     * 查询全部未删除的菜单, 按父级、排序值与主键升序排列
     *
     * @return 菜单实体列表
     */
    public List<SysMenu> selectAllSorted() {
        return sysMenuMapper.selectList(Wrappers.<SysMenu>lambdaQuery()
                .orderByAsc(SysMenu::getParentId)
                .orderByAsc(SysMenu::getSort)
                .orderByAsc(SysMenu::getId));
    }

    /**
     * 菜单实体列表转视图对象列表
     *
     * @param menus 菜单实体列表
     * @return 菜单视图对象列表
     */
    public List<MenuVO> toVOList(List<SysMenu> menus) {
        List<MenuVO> list = new LinkedList<>();
        for (SysMenu menu : menus) {
            list.add(sysMenuConverter.toVO(menu));
        }
        return list;
    }

    /**
     * 校验名称与类型等基础必填项
     *
     * @param formDTO 菜单表单参数
     */
    private void validateBase(MenuFormDTO formDTO) {
        if (StrUtil.isBlank(formDTO.getName())) {
            throw new ServiceException("菜单名称不能为空");
        }
        if (StrUtil.isBlank(formDTO.getType())) {
            throw new ServiceException("菜单类型不能为空");
        }
        validateType(formDTO.getType());
        MenuTypeEnum type = MenuTypeEnum.getByValue(formDTO.getType());
        if (!MenuTypeEnum.BUTTON.equals(type) && StrUtil.isBlank(formDTO.getPath())) {
            throw new ServiceException("目录与页面必须填写路由地址");
        }
        if (MenuTypeEnum.MENU.equals(type) && StrUtil.isBlank(formDTO.getComponent())) {
            throw new ServiceException("页面必须填写前端组件标识");
        }
    }

    /**
     * 校验菜单类型是否合法
     *
     * @param type 菜单类型
     */
    private void validateType(String type) {
        if (ObjectUtil.isNull(MenuTypeEnum.getByValue(type))) {
            throw new ServiceException("菜单类型不合法, 仅支持 DIR / MENU / BUTTON");
        }
    }

    /**
     * 校验父子菜单的层级关系: 按钮不能作为父级, 目录与页面不能挂在按钮下
     *
     * @param type   当前菜单类型
     * @param parent 父菜单实体, 顶级菜单时为 null
     */
    private void validateHierarchy(String type, SysMenu parent) {
        if (ObjectUtil.isNull(parent)) {
            if (MenuTypeEnum.BUTTON.getValue().equalsIgnoreCase(type)) {
                throw new ServiceException("按钮必须挂在目录或页面下");
            }
            return;
        }
        if (MenuTypeEnum.BUTTON.getValue().equalsIgnoreCase(parent.getType())) {
            throw new ServiceException("按钮不能作为上级菜单");
        }
        if (MenuTypeEnum.BUTTON.getValue().equalsIgnoreCase(type)
                && MenuTypeEnum.DIR.getValue().equalsIgnoreCase(parent.getType())) {
            throw new ServiceException("按钮必须挂在页面下");
        }
    }

    /**
     * 获取父菜单, 父级 ID 不是顶级时要求父菜单必须存在
     *
     * @param parentId 父级 ID
     * @return 父菜单实体, 顶级时返回 null
     */
    private SysMenu getParentIfNecessary(long parentId) {
        if (parentId == RbacConstant.ROOT_PARENT_ID) {
            return null;
        }
        SysMenu parent = sysMenuMapper.selectById(parentId);
        if (ObjectUtil.isNull(parent)) {
            throw new ServiceException("上级菜单不存在");
        }
        return parent;
    }

    /**
     * 判断指定菜单是否为目标菜单的下级, 用于阻止把节点移动到自己的子树下
     *
     * @param ancestorId  当前菜单ID
     * @param targetId    目标父菜单ID
     * @return 目标父菜单是否位于当前菜单的子树中
     */
    private boolean isDescendant(Long ancestorId, Long targetId) {
        Set<Long> visited = new HashSet<>();
        Long current = targetId;
        while (ObjectUtil.isNotNull(current) && current != RbacConstant.ROOT_PARENT_ID) {
            if (!visited.add(current)) {
                return false;
            }
            if (ObjectUtil.equal(ancestorId, current)) {
                return true;
            }
            SysMenu menu = sysMenuMapper.selectById(current);
            if (ObjectUtil.isNull(menu)) {
                return false;
            }
            current = menu.getParentId();
        }
        return false;
    }

    /**
     * 获取存在的菜单
     *
     * @param id 菜单ID
     * @return 菜单实体 {@link SysMenu}
     */
    private SysMenu getExistMenu(Long id) {
        SysMenu menu = sysMenuMapper.selectById(id);
        if (ObjectUtil.isNull(menu)) {
            throw new ServiceException("菜单不存在");
        }
        return menu;
    }
}
