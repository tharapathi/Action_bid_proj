package com.sapient.auction.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * UserVO model/Json class, to transfer the User information as Json message.
 *
 * Created by dpadal on 11/10/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
public class UserVO {

    private String id;
    @NotBlank(message = "Password should not be blank")
    private String password;
    @NotBlank(message = "FirstName should not be blank")
    private String firstName;
    private String lastName;
    @NotBlank(message = "Email should not be blank")
    //@ValidEmail(message = "Not a valid email")
    private String email;
    @NotBlank(message = "Contact should not be blank")
    private String contact;
    private String address;

    public UserVO(Builder builder) {
        this.id = builder.id;
        this.password = builder.password;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.contact = builder.contact;
        this.address = builder.address;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String id;
        private String password;
        private String firstName;
        private String lastName;
        private String email;
        private String contact;
        private String address;

        private Builder(){}

        public UserVO build() {
            return new UserVO(this);
        }

        public Builder withId(String id) {
            this.id =id;
            return this;
        }

        public Builder withPassword(String password) {
            this.password =password;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName =firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName =lastName;
            return this;
        }

        public Builder withEmail(String email) {
            this.email =email;
            return this;
        }

        public Builder withContact(String contact) {
            this.contact =contact;
            return this;
        }

        public Builder withAddress(String address) {
            this.address = address;
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

        UserVO userVO = (UserVO) o;

        return email.equals(userVO.email);

    }

    @Override
    public int hashCode() {
        return  email != null ? email.hashCode() : 0;
    }
}
