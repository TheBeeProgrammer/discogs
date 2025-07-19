package com.clara.domain.model

import java.io.IOException

class NetworkUnavailableException(message: String = "No network available :(") : IOException(message)