package com.ranyk.vt.boot.example.system.user.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * CLASS_NAME: UserVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户信息返回结果
 * @date: 2026-11-16
 */
@Data
public class UserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2459713794810741373L;

    /**
     * 用户ID
     */
    private Long id;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 登录名
     */
    private String username;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 主角色编码(取自 t_user.role, 用于兼容与鉴权会话)
     */
    private String role;
    /**
     * 角色ID集合
     */
    private List<Long> roleIds;
    /**
     * 角色编码集合
     */
    private List<String> roleCodes;
    /**
     * 角色名称集合
     */
    private List<String> roleNames;
    /**
     * 状态: 1-正常 0-禁用
     */
    private Integer status;
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
    /**
     * 最后登录IP
     */
    private String lastLoginIp;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 备注
     */
    private String remark;
}
