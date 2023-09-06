package me.muse.CrezyBackend.domain.account.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="loginType")
@NoArgsConstructor
public class AccountLoginType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Getter
    private LoginType loginType;

    public AccountLoginType(LoginType loginType) {
        this.loginType = loginType;
    }
}
