package com.fawwaz_bank.bill_splitter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "group_members")
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    private BillGroup group;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    public GroupMember() {
    }

    public GroupMember(BillGroup group, User user) {
        this.group = group;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public BillGroup getGroup() {
        return group;
    }

    public User getUser() {
        return user;
    }

    public void setGroup(BillGroup group) {
        this.group = group;
    }

    public void setUser(User user) {
        this.user = user;
    }
}