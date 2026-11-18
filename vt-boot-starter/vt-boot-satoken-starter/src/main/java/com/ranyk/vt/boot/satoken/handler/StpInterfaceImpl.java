package com.ranyk.vt.boot.satoken.handler;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.ranyk.vt.boot.base.constant.SessionKeyEnum;
import com.ranyk.vt.boot.satoken.config.properties.SaTokenProperties;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CLASS_NAME: StpInterfaceImpl.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Sa-Token 权限数据加载默认实现, 从会话中读取角色并映射权限。
 * <p>
 * 角色在登录建立会话时写入 {@link SessionKeyEnum#ROLE} 对应的会话属性,
 * 管理员角色({@code sa-token.admin-role})返回全部权限通配符({@code sa-token.all-permission}),
 * 其他角色默认不授予额外权限。应用可通过自行注册 {@link StpInterface} Bean 覆盖该默认实现。
 * </p>
 * @date: 2026-11-16
 */
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    /**
     * SA-TOKEN 配置属性对象
     */
    private final SaTokenProperties saTokenProperties;

    /**
     * 获取指定登录账号的权限标识列表。管理员返回通配权限(默认 "*")表示拥有全部权限,
     * 其他角色暂不授予额外权限, 返回空列表。
     *
     * @param loginId   登录账号标识
     * @param loginType 登录类型(如 login / admin)
     * @return 权限标识列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        String role = getRole(loginId);
        if (ObjectUtil.equal(saTokenProperties.getAdminRole(), role)) {
            return Collections.singletonList(saTokenProperties.getAllPermission());
        }
        return Collections.emptyList();
    }

    /**
     * 获取指定登录账号的角色列表。角色在登录建立会话时写入会话属性, 这里从会话中读取并返回。
     *
     * @param loginId   登录账号标识
     * @param loginType 登录类型(如 login / admin)
     * @return 角色编码列表, 会话中无角色时返回空列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        String role = getRole(loginId);
        List<String> roles = new ArrayList<>(1);
        if (ObjectUtil.isNotNull(role)) {
            roles.add(role);
        }
        return roles;
    }

    /**
     * 从会话中读取角色
     *
     * @param loginId 登录标识
     * @return 角色编码, 不存在时返回 null
     */
    private String getRole(Object loginId) {
        SaSession session = StpUtil.getSessionByLoginId(loginId, false);
        if (ObjectUtil.isNull(session)) {
            return null;
        }
        Object role = session.get(SessionKeyEnum.ROLE.getValue());
        return ObjectUtil.isNull(role) ? null : Convert.toStr(role);
    }
}
