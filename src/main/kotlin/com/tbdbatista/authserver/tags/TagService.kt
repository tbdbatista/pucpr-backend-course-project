package com.tbdbatista.authserver.tags

import com.tbdbatista.authserver.api.ConflictException
import com.tbdbatista.authserver.api.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TagService(val repository: TagRepository) {
    private val log = LoggerFactory.getLogger(TagService::class.java)

    fun insert(tag: Tag): Tag? {
        val name = tag.name.uppercase()
        if (repository.findByName(name) != null) {
            log.warn("Tag insert conflict: {}", name)
            return null
        }
        tag.name = name
        log.info("Creating tag: {}", name)
        return repository.save(tag)
    }

    fun findAll() = repository.findAll()

    fun findByIdOrNull(id: Long) = repository.findByIdOrNull(id)

    fun update(id: Long, tag: Tag): Tag? {
        val existing = repository.findByIdOrNull(id) ?: return null
        val name = tag.name.uppercase()
        val nameOwner = repository.findByName(name)
        if (nameOwner != null && nameOwner.id != id) {
            throw ConflictException("tag name already exists")
        }
        existing.name = name
        existing.description = tag.description
        log.info("Updating tag: {}", id)
        return repository.save(existing)
    }

    fun delete(id: Long): Boolean {
        val tag = repository.findByIdOrNull(id) ?: return false
        log.info("Deleting tag: {}", id)
        repository.delete(tag)
        return true
    }

    fun findByNameOrThrow(name: String): Tag {
        val tag = repository.findByName(name.uppercase()) ?: throw NotFoundException("tag not found")
        return tag
    }
}

