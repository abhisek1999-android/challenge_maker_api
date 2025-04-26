package com.abhisek.challenge_maker.challenge_maker.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_contact_number")
    private String userContactNumber;

    @Column(name = "user_email")
    private String email;

    @Column(name = "password")
    private String password;

    private String roles;  // Role-based access control (USER or ADMIN)

}

