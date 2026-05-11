package com.tbdbatista.authserver.users

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
import java.net.IDN

@RestController
@RequestMapping("/users")
class UserController(val userService: UserService) {
    @GetMapping("/ping")
    open fun ping() = mapOf("status" to "ok")

    @PostMapping
    @ApiResponse(responseCode = "201")
    fun insert(@RequestBody user: User) = userService.insert(user)
        ?.let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
        ?: ResponseEntity.badRequest().build()

    @GetMapping
    fun list(@RequestParam sortDir: String?, @RequestParam role: String?) =
        if (role != null) {
            userService.findByRole(role)
                .let { ResponseEntity.ok(it) }
        }
        else {
            SortDir.findOrNull(sortDir ?: "ASC")
                ?.let { userService.findAll(it)}
                ?.let { ResponseEntity.ok(it) }
                ?: ResponseEntity.badRequest().build()
        }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) =
        userService.findByIdOrNull(id)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): ResponseEntity<Void>  =
        if (userService.delete(id)) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }

    @PutMapping("/{id}/roles/{roleName}")
    fun grant(@PathVariable id: Long, @PathVariable roleName: String): ResponseEntity<Void> =
    userService.addRole(id, roleName)
        ?.let { if (it) ResponseEntity.ok().build() else ResponseEntity.noContent().build() }
        ?: ResponseEntity.badRequest().build()
}