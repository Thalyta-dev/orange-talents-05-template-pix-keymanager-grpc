package com.zup.servicosExternos.sistemaBbc

import com.zup.registraChave.TipoChave

enum class KeyType {

    CPF {
        override fun retornaTipoContaPix(): TipoChave {

            return TipoChave.CPF

        }

    },
    RANDOM {

        override fun retornaTipoContaPix(): TipoChave {

            return TipoChave.ALEATORIA

        }
    },
    EMAIL {
        override fun retornaTipoContaPix(): TipoChave {

            return TipoChave.EMAIL

        }
    },

    PHONE {

        override fun retornaTipoContaPix(): TipoChave {

            return TipoChave.TELEFONE

        }
    };

    abstract fun retornaTipoContaPix(): TipoChave
}