package com.zup.annotationValidacoes

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Constraint(validatedBy = [ChavePixValidador::class])
annotation class ValidChavePix(
    val message: String = "chave pix invalida",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
