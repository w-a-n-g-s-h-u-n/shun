 package org.wangshun.shun.sample.curd.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum GenderEnum implements IEnum<Integer> {
    MALE, FEMALE, NEUTER, UNKNOWN;

    @Override
    public Integer getValue() {
        return ordinal();
    }
}
