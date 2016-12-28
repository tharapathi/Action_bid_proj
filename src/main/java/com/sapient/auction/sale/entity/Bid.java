package com.sapient.auction.sale.entity;

import com.sapient.auction.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Bid entity class, mapped to BID table.
 *
 * Created by dpadal on 11/16/2016.
 */
@Entity
@Table(name = "BID")
@Setter @Getter
public class Bid {

    @Id
    @Column(name = "ID")
    @GenericGenerator(strategy = "increment", name = "increment")
    @GeneratedValue(generator = "increment")
    private Integer id;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    @Column(name = "TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false, referencedColumnName = "ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SALE_ID", nullable = false, referencedColumnName = "ID")
    private Sale sale;
}
