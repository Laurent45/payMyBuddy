package com.outsider.paymybuddy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_user", nullable = false)
    private Long idUser;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    @JoinTable(name = "connection", joinColumns = {
            @JoinColumn(name = "id_user"
                    , referencedColumnName = "id_user"
                    , nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "id_user_connected",
                    referencedColumnName = "id_user", nullable = false)})
    private Set<User> usersConnected;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            fetch = FetchType.LAZY,
            mappedBy = "user")
    private Set<Transfer> transfers;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            fetch = FetchType.LAZY,
            mappedBy = "userDebtor")
    private Set<Transaction> transactionsDebtor;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            fetch = FetchType.LAZY,
            mappedBy = "userCreditor")
    private Set<Transaction> transactionsCreditor;

}
