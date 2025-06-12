package com.gdb.models.users;

import java.util.List;

public class Role {
    private int roleId;
    private RoleType roleType;
    List<Permission> permissions;
    public int getRoleId() {
        return roleId;
    }
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    public RoleType getRoleType() {
        return roleType;
    }
    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }
    public List<Permission> getPermissions() {
        return permissions;
    }
    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
