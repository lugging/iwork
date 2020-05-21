package com.yuntongxun.iwork.ds.entity;

import com.yuntongxun.iwork.dao.api.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author liugang
 * @since 2020-01-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class YtxUserInfoEntity extends BaseEntity {

    private static final long serialVersionUID = 4415615952242800534L;
    /**
     * 自增长ID
     */
    private Long userId;

    private String phoneNum;

    private Integer userStatus;

    private String countryCode;

    private String appVersion;

    private String completeCode;

    private String deviceType;

    private String deviceAgent;

    private String appId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public static final String PHONE_NUM = "phone_num";

    public static final String USER_STATUS = "user_status";
}
