package br.com.zup.edu.grpc.handlers

import io.grpc.StatusRuntimeException

class PixKeyExistingException(override val message: String) : RuntimeException(message)