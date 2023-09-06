package me.muse.CrezyBackend.domain.account.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name="roleType")
@NoArgsConstructor
public class AccountRoleType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Getter
    private RoleType roleType;

    public AccountRoleType(RoleType roleType) {
        this.roleType = roleType;
    }
}
