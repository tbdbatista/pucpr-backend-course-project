package com.tbdbatista.authserver.projects

import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ProjectRepository: JpaRepository<Project, Long> {
    fun findByName(name: String): Project?

    @Query(
        """
        select distinct p from Project p
        left join p.tags t
        where (:tag is null or t.name = :tag)
        and (:name is null or lower(p.name) like lower(concat('%', :name, '%')))
        """
    )
    fun search(@Param("tag") tag: String?, @Param("name") name: String?, sort: Sort): List<Project>
}

