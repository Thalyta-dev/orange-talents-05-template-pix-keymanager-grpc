package com.zup.registraChave

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface PixRepository: JpaRepository<ChavePix, UUID> {

    fun existsByValorChave(chave: String?): Boolean

}
