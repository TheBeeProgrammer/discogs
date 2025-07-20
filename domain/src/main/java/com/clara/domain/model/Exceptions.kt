package com.clara.domain.model

import java.io.IOException

class NetworkUnavailableException(message: String = "No network available :(") :
    IOException(message)
class InternalServerErrorException(message: String = "Unauthorized") : Exception(message)
class UnknownErrorException(message: String = "Unknown error") : Exception(message)
