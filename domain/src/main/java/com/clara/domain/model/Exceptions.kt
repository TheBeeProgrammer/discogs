package com.clara.domain.model

import java.io.IOException

class NetworkUnavailableException(message: String = "No network available :(") :
    IOException(message)
class UnauthorizedException(message: String = "Unauthorized") : Exception(message)
class ForbiddenException(modificationException: String = "Forbidden") :
    Exception(modificationException)
