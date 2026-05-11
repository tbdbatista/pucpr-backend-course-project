package com.tbdbatista.authserver.projects

import com.tbdbatista.authserver.tags.Tag
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

@Entity
@Table(name = "projectTable")
class Project (
    @Id
    @GeneratedValue
    var id: Long? = null,

    @Column(nullable = false, unique = true)
    var name: String,

    @Column(nullable = false)
    var description: String,

    @ManyToMany
    @JoinTable(
        name = "ProjectTag",
        joinColumns = [JoinColumn(name = "idProject", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "idTag", referencedColumnName = "id")]
    )
    var tags: MutableSet<Tag> = mutableSetOf(),
)

