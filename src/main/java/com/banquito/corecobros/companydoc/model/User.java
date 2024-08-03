package com.banquito.corecobros.companydoc.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "users")
@CompoundIndexes({
        @CompoundIndex(name = "useridx_user", def = "{'user': 1, 'companyId': 1}", unique = true)
})
public class User {

    @Id
    private String id;
    @Indexed
    private String companyId;
    @Indexed(unique = true)
    private String uniqueId;
    private String firstName;
    private String lastName;
    @Indexed
    private String user;
    private String password;
    private String resetCode;
    private LocalDate createDate;
    @Indexed
    private String email;
    private String role;
    private String status;
    private LocalDateTime lastConnection;
    private int failedAttempt;
    private String userType;
    private boolean isFirstLogin;
    private String oldPassword;
    private String newPassword;
    private String message;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
