package com.tbdbatista.authserver.projects

import com.tbdbatista.authserver.api.ConflictException
import com.tbdbatista.authserver.api.NotFoundException
import com.tbdbatista.authserver.tags.TagService
import com.tbdbatista.authserver.users.SortDir
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProjectService(
    val repository: ProjectRepository,
    val tagService: TagService
) {
    private val log = LoggerFactory.getLogger(ProjectService::class.java)

    fun insert(project: Project): Project? {
        if (repository.findByName(project.name) != null) {
            log.warn("Project insert conflict: {}", project.name)
            return null
        }
        log.info("Creating project: {}", project.name)
        return repository.save(project)
    }

    fun findByIdOrNull(id: Long) = repository.findByIdOrNull(id)

    fun update(id: Long, project: Project): Project? {
        val existing = repository.findByIdOrNull(id) ?: return null
        val nameOwner = repository.findByName(project.name)
        if (nameOwner != null && nameOwner.id != id) {
            throw ConflictException("project name already exists")
        }
        existing.name = project.name
        existing.description = project.description
        log.info("Updating project: {}", id)
        return repository.save(existing)
    }

    fun delete(id: Long): Boolean {
        val project = repository.findByIdOrNull(id) ?: return false
        log.info("Deleting project: {}", id)
        repository.delete(project)
        return true
    }

    fun search(tag: String?, name: String?, sortBy: ProjectSortBy, sortDir: SortDir): List<Project> {
        val sort = when (sortDir) {
            SortDir.ASC -> Sort.by(sortBy.prop).ascending()
            SortDir.DESC -> Sort.by(sortBy.prop).descending()
        }
        return repository.search(tag?.uppercase(), name, sort)
    }

    @Transactional
    fun addTag(id: Long, tagName: String): Boolean? {
        val project = repository.findByIdOrNull(id) ?: return null
        val tag = tagService.findByNameOrThrow(tagName)
        if (project.tags.any { it.name == tag.name }) { return false }
        project.tags.add(tag)
        log.info("Adding tag {} to project {}", tag.name, id)
        repository.save(project)
        return true
    }

    @Transactional
    fun removeTag(id: Long, tagName: String): Boolean? {
        val project = repository.findByIdOrNull(id) ?: return null
        val tag = tagService.findByNameOrThrow(tagName)
        val removed = project.tags.removeIf { it.name == tag.name }
        if (!removed) { return false }
        log.info("Removing tag {} from project {}", tag.name, id)
        repository.save(project)
        return true
    }

    fun findByIdOrThrow(id: Long): Project =
        repository.findByIdOrNull(id) ?: throw NotFoundException("project not found")
}

