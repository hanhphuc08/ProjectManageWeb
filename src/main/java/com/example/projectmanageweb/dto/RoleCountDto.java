package com.example.projectmanageweb.dto;

public class RoleCountDto {
	private String roleName;
    private long count;

    public RoleCountDto() {}

    public RoleCountDto(String roleName, long count) {
        this.roleName = roleName;
        this.count = count;
    }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public long getCount() { return count; }
    public void setCount(long count) { this.count = count; }

}
