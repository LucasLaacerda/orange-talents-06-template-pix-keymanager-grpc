package br.com.zup.edu.grpc.handlers


class PixKeyExistingException(override val message: String) : RuntimeException(message)