package com.tbdbatista.authserver.users

import com.tbdbatista.authserver.roles.Role
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

@Entity
@Table(name = "userTable")
class User (
    @Id
    @GeneratedValue
    var id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    var password: String,

    @ManyToMany
    @JoinTable(
        name = "UserRole",
        joinColumns = [JoinColumn(name = "idUser", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "idRole", referencedColumnName = "id")]
    )
    var roles: MutableSet<Role> = mutableSetOf(),
)