package com.fawwaz_bank.bill_splitter.dto;

public class AddGroupMemberRequest {

    private Long groupId;
    private Long userId;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}