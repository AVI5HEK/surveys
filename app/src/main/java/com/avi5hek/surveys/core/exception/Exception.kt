package com.avi5hek.surveys.core.exception

class NetworkConnectionException : Exception() {

  override val message: String?
    get() = "Check your Internet connection."
}

class ServerException : Exception { constructor() : super()
  constructor(message: String) : super(message)
}

class TokenExpiredException : Exception { constructor() : super()
  constructor(message: String) : super(message)
}
