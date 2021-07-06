package com.zup.registraChave

import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface PixRepository: JpaRepository<ChavePix, UUID> {

    fun existsByValorChave(chave: String?): Boolean

    @Query(value = "SELECT * FROM conta WHERE tipo = :tipo and numero = :numero", nativeQuery = true)
    fun findByConta(numero: String, tipo: String): Conta?

}
