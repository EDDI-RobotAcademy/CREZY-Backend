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
    private String name;
    private String nickname;
    private String password;
    private String email;

    public Account(String name, String nickname, String email) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
    }

}
