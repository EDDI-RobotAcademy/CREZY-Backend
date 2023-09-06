package me.muse.CrezyBackend.domain.account.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
public class Profile {
    @Id
    @Column(name = "profileId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;
    @Setter
    private String nickname;
    private String password;
    private String email;
    @Setter
    private String profileImageName;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    public Profile(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }

    public Profile(String nickname, String email, String profileImageName, Account account) {
        this.nickname = nickname;
        this.email = email;
        this.profileImageName = profileImageName;
        this.account = account;
    }
}
