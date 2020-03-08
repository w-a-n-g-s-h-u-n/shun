 package org.wangshun.shun.sample.curd.entity.PO;

import java.time.Instant;

import org.wangshun.shun.db.base.entity.BaseCurdEntity;
import org.wangshun.shun.sample.curd.enums.GenderEnum;

import lombok.Data;

@Data
public class User extends BaseCurdEntity<User> {
    private static final long serialVersionUID = 1L;
    private String name;
    private String nick;
    private GenderEnum gender;
    private Instant birthday;
    private String email;
    private String username;
    private String password;
    private Boolean disabled;
}
