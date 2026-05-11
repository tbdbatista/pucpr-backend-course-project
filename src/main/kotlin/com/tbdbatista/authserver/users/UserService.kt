package com.tbdbatista.authserver.users

import com.tbdbatista.authserver.roles.Role
import com.tbdbatista.authserver.roles.RoleRepository
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    val repository: UserRepository,
    val roleRepository: RoleRepository
) {
    fun insert(user: User): User? {
        if (repository.findByEmail(user.email) != null) {
            return null
        }
        return repository.save(user)
    }

    fun findAll(sortDir: SortDir) = when (sortDir) {
        SortDir.ASC -> repository.findAll(Sort.by("name").ascending())
        SortDir.DESC -> repository.findAll(Sort.by("name").descending())
    }

    fun findByIdOrNull(id: Long) = repository.findByIdOrNull(id)

    fun delete(id: Long): Boolean {
        val user = repository.findByIdOrNull(id) ?: return false
        repository.delete(user)
        return true
    }

    fun addRole(id: Long, roleName: String): Boolean? {
        val role = roleName.uppercase()
        val user = repository.findByIdOrNull(id) ?: return null
        if (user.roles.any { it.name == role }) { return false }
        val checkedRole = roleRepository.findByName(role) ?: return null
        user.roles.add(checkedRole)
        repository.save(user)
        return true
    }
}