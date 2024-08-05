package com.ohapps.snobbinapi.domain.common

import java.lang.RuntimeException

class InvalidUserToken(message: String) : RuntimeException(message)
class DataNotFound(message: String) : RuntimeException(message)
class AccessDenied(message: String) : RuntimeException(message)
class BadRequest(message: String) : RuntimeException(message)
