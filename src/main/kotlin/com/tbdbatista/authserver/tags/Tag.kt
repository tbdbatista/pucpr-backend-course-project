package com.tbdbatista.authserver.tags

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class Tag (
    @Id @GeneratedValue
    var id: Long? = null,

    @Column(nullable=false, unique = true)
    var name: String,

    @Column(nullable=false)
    var description: String
)

