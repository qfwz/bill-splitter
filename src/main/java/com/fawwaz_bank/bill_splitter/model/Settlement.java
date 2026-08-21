package com.fawwaz_bank.bill_splitter.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "settlements")
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    private BillGroup group;

    @ManyToOne(optional = false)
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne(optional = false)
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    public Settlement() {
    }

    public Settlement(
            BillGroup group,
            User fromUser,
            User toUser,
            BigDecimal amount) {

        this.group = group;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public BillGroup getGroup() {
        return group;
    }

    public User getFromUser() {
        return fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setGroup(BillGroup group) {
        this.group = group;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Settlement{" +
                "from=" + getFromUser().getUsername() +
                ", to=" + getToUser().getUsername() +
                ", amount=" + amount +
                '}';
    }
}