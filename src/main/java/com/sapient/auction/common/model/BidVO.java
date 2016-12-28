package com.sapient.auction.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by dpadal on 11/14/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
public class BidVO {

    private Integer id;
    private UserVO user;
    private SaleVO sale;
    @NotNull(message = "Price should not be blank")
    private BigDecimal price;
    private Date time;

    public BidVO(Builder builder) {
        this.id = builder.id;
        this.user = builder.user;
        this.sale = builder.sale;
        this.price = builder.price;
        this.time = builder.time;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private UserVO user;
        private SaleVO sale;
        private BigDecimal price;
        private Date time;

        public Builder() {
        }

        public BidVO build() {
            return new BidVO(this);
        }

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withTime(Date time) {
            this.time = time;
            return this;
        }

        public Builder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder withUser(UserVO user) {
            this.user = user;
            return this;
        }

        public Builder withSale(SaleVO sale) {
            this.sale = sale;
            return this;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BidVO bidVO = (BidVO) o;

        if (!id.equals(bidVO.id)) {
            return false;
        }
        if (user.getEmail() != null && bidVO.getUser().getEmail() != null) {
            return user.getEmail().equals(bidVO.user.getEmail());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + user.getEmail() != null ? user.getEmail().hashCode() : 0;
        return result;
    }
}
