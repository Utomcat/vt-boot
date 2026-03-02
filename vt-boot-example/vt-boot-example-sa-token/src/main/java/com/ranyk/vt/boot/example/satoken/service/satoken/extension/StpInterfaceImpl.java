package com.ranyk.vt.boot.example.satoken.service.satoken.extension;

import cn.dev33.satoken.stp.StpInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CLASS_NAME: StpInterfaceImpl.java
 *
 * @author ranyk
 * @version V1.0
 * @description: sa-token 权限验证扩展
 * @date: 2026-03-02
 */
@Slf4j
@Service
public class StpInterfaceImpl implements StpInterface {

    /**
     * 返回指定账号id所拥有的权限码集合
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     * @return 该账号id具有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return List.of();
    }

    /**
     * 返回指定账号id所拥有的角色标识集合
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     * @return 该账号id具有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return List.of();
    }
}
