package com.outsider.paymybuddy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "balance", columnDefinition = "DECIMAL(10, 2) default 0")
    private float balance;

    /*
        Fields about to relations between entities.
     */

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "connection",
            joinColumns = {
            @JoinColumn(name = "id_user", referencedColumnName = "id_user")},
            inverseJoinColumns = {
            @JoinColumn(name = "id_user_connected", referencedColumnName = "id_user")})
    private Set<User> usersConnected;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Transfer> transfers;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userDebtor")
    private Set<Transaction> transactionsDebtor;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userCreditor")
    private Set<Transaction> transactionsCreditor;


    public void removeConnection(User user) {
        this.getUsersConnected().remove(user);
    }

    public void addConnection(User user) {
        this.getUsersConnected().add(user);
    }

    public User(String lastName, String firstName, String email, String password) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
    }

    public User(String lastName, String firstName, String email, String password, float balance) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Float.compare(user.balance, balance) == 0
                && lastName.equals(user.lastName)
                && firstName.equals(user.firstName)
                && email.equals(user.email)
                && password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastName, firstName, email, password, balance);
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                '}';
    }
}
