package com.tbdbatista.authserver.projects

enum class ProjectSortBy(val prop: String) {
    ID("id"),
    NAME("name");

    companion object {
        fun findOrNull(value: String) =
            entries.find { it.name == value.uppercase() }
    }
}

