package com.sapient.auction.sale.entity;

import com.sapient.auction.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

/**
 * Entity class mapped to SALE data table.
 *
 * Created by dpadal on 11/14/2016.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "SALE")
@Setter
@Getter
public class Sale implements Serializable {

	@Id
	@GenericGenerator(strategy = "increment", name = "increment")
	@GeneratedValue(generator = "increment")
	private Long id;

	@Column(name = "START_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	@Column(name = "END_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	@Column(name = "PRICE", nullable = false)
	private BigDecimal price;

	@Column(name = "PRODUCT_ID", nullable = false, length = 25)
	private String productId;

	@Column(name = "PRODUCT_NAME", nullable = false, length = 25)
	private String productName;

	@Column(name = "PRODUCT_TYPE", nullable = false, length = 25)
	private String productType;

	@Column(name = "PRODUCT_DESC", length = 256)
	private String productDesc;

	@Column(name = "PRODUCT_IMAGE", nullable = false, length = 256)
	private String productImageUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = false, referencedColumnName = "ID")
	private User user;

	@OneToMany(mappedBy = "sale", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Bid> bids;
}
