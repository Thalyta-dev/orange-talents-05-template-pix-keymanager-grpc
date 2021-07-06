package com.zup.annotationValidacoes

import com.zup.registraChave.PixRequestValida
import com.zup.registraChave.TipoChave
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton

@Singleton
class ChavePixValidador: ConstraintValidator<ValidChavePix,PixRequestValida>{


    override fun isValid(
        value: PixRequestValida,
        annotationMetadata: AnnotationValue<ValidChavePix>,
        context: ConstraintValidatorContext
    ): Boolean {

        if(value.tipoChave == TipoChave.ALEATORIA) return true

        return value.tipoChave.validaChave(value.valorChave!!)
    }
}
