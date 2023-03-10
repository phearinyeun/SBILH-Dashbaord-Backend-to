package com.sbilhbank.insur.entity.primary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 2000)
    private String accessToken;
    private Date accessTokenIssueAt;
    private Date accessTokenExpireAt;
    @Column(unique = true, length = 1000)
    private String refreshToken;
    private Date refreshTokenIssueAt;
    private Date refreshTokenExpireAt;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private boolean isRevoke;
}
