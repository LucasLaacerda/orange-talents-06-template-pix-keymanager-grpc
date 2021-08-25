package br.com.zup.edu.pix.validation

import br.com.zup.edu.RegisterPixKeyRequest
import java.lang.IllegalArgumentException

fun RegisterPixKeyRequest.validated(){

    if(keyValue.trim().length>77){
        throw  IllegalArgumentException("Chave Pix invalida! Chave deve ter no maximo 77 caracteres")
    }


    when(keyType){
        RegisterPixKeyRequest.KeyType.CPF -> {
            if (!this.keyValue.matches("^[0-9]{11}\$".toRegex()))
                throw  IllegalArgumentException("cpf invalido! formato esperado: 12345678901")
        }
        RegisterPixKeyRequest.KeyType.EMAIL -> {
            if (!keyValue.matches("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
                    .toRegex())) throw  IllegalArgumentException("email invalido")
        }
        RegisterPixKeyRequest.KeyType.CELULAR -> {
            if (!keyValue.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex()))
                throw  IllegalArgumentException("celular invalido! formato esperado: +5585988714077")
        }

    }
}