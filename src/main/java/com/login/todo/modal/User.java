package com.login.todo.modal;

import java.sql.Ref;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "User_Table")
@Data
public class User implements UserDetails{
    @Id
    private String user_id;

    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String password;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<TodoItem> todoItems;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private RefreshToken refreshToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
    @Override
    public String getUsername(){
        return this.email;
    }
    @Override
    public boolean isAccountNonExpired() {
       return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;

    }

}
