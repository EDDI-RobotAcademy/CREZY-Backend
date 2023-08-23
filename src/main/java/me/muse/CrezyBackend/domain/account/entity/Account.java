package me.muse.CrezyBackend.domain.account.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private String password;
    private String email;

    public Account(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }

}
