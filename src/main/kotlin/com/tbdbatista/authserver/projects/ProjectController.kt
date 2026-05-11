package com.tbdbatista.authserver.projects

import com.tbdbatista.authserver.users.SortDir
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/projects")
class ProjectController(val projectService: ProjectService) {
    @PostMapping
    @ApiResponse(responseCode = "201")
    fun insert(@RequestBody project: Project) = projectService.insert(project)
        ?.let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
        ?: ResponseEntity.badRequest().build()

    @GetMapping
    fun list(
        @RequestParam tag: String?,
        @RequestParam name: String?,
        @RequestParam sortDir: String?,
        @RequestParam sortBy: String?
    ) =
        SortDir.findOrNull(sortDir ?: "ASC")
            ?.let { dir ->
                ProjectSortBy.findOrNull(sortBy ?: "NAME")
                    ?.let { by ->
                        projectService.search(tag, name, by, dir)
                            .let { ResponseEntity.ok(it) }
                    }
            }
            ?: ResponseEntity.badRequest().build()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) =
        projectService.findByIdOrNull(id)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody project: Project) =
        projectService.update(id, project)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): ResponseEntity<Void> =
        if (projectService.delete(id)) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }

    @PutMapping("/{id}/tags/{tagName}")
    fun addTag(@PathVariable id: Long, @PathVariable tagName: String): ResponseEntity<Void> =
        projectService.addTag(id, tagName)
            ?.let { if (it) ResponseEntity.ok().build() else ResponseEntity.noContent().build() }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}/tags/{tagName}")
    fun removeTag(@PathVariable id: Long, @PathVariable tagName: String): ResponseEntity<Void> =
        projectService.removeTag(id, tagName)
            ?.let { if (it) ResponseEntity.ok().build() else ResponseEntity.noContent().build() }
            ?: ResponseEntity.notFound().build()
}

