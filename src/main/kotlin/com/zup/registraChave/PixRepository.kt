package com.zup.registraChave

import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface PixRepository: JpaRepository<ChavePix, UUID> {

    fun findByValorChave(chave: String): Optional<ChavePix>

    fun findByClientId(clienteId: String): Optional<ChavePix>

    @Query(value = "SELECT * FROM chave_pix WHERE client_id = :clienteId", nativeQuery = true)
    fun chavesClientes( clienteId: String): List<ChavePix>


    @Query(value = "SELECT * FROM chave_pix WHERE pix_id = :pixId and client_id = :clienteId", nativeQuery = true)
    fun clientDonoDaChave(pixId: UUID, clienteId: String): ChavePix?

    @Query(value = "SELECT * FROM conta WHERE tipo = :tipo and numero = :numero", nativeQuery = true)
    fun findByConta(numero: String, tipo: String): Conta?

}
