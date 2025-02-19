package com.login.task.modal;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(unique = true)
    private String email;

    @Column
    private String name;

    @Column
    private String password;
    
    @Column
    private String role = "USER";

    @Column
    private LocalDateTime created_at;

    @Column
    private LocalDateTime updated_at;

    

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Task> tasks;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private RefreshToken refreshToken;

    public String getName(){
        return this.name;
    }
    public String setName(String name){
        return this.name= name;
    }
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
