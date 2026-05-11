package com.tbdbatista.authserver.api

open class ApiException(message: String): RuntimeException(message)

class BadRequestException(message: String): ApiException(message)
class UnauthorizedException(message: String): ApiException(message)
class NotFoundException(message: String): ApiException(message)
class ConflictException(message: String): ApiException(message)

