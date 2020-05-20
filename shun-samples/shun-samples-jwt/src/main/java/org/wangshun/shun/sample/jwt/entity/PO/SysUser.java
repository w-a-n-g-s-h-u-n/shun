package org.wangshun.shun.sample.jwt.entity.PO;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.wangshun.shun.db.base.entity.BaseCurdEntity;

import java.util.List;

/**
 * 用户
 */
@Data
@NoArgsConstructor
public class SysUser extends BaseCurdEntity<SysUser> implements CredentialsContainer, UserDetails {
    public SysUser(Long id) {
        this.id = id;
    }

    public SysUser(String username) {
        this.username = username;
    }

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 登录名
     */
    protected String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobileNumber;

    /**
     * 密码
     */
    protected String password;

    /**
     * 名称
     */
    private String name;

    /**
     * 呢称
     */
    private String nick;

    /**
     * 是否禁用
     */
    protected Boolean disabled;
    /**
     * 是否锁定
     */
    protected Boolean locked;

    /**
     * 拥有的权限
     */
    @TableField(exist = false)
    private List<GrantedAuthority> authorities = CollUtil.newLinkedList();

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    public static class SysUserTypeHandler extends BaseDatabaseEntityTypeHandler<SysUser> {
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !disabled;
    }
}
