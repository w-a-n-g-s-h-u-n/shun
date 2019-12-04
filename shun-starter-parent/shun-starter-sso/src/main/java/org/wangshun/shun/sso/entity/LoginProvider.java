package org.wangshun.shun.sso.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginProvider {
    private String name;
    private String description;
    private String loginUrl;
}
