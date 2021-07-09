package com.zup.servicosExternos.sistemaBbc

import com.zup.registraChave.TipoChave

enum class KeyType {

    CPF {
        override fun retornaTipoContaPix(): TipoChave {

            return TipoChave.CPF

        }

        override fun retornaTipoContaPixProto(): com.zup.TipoChave {
            return com.zup.TipoChave.CPF
        }

    },
    RANDOM {

        override fun retornaTipoContaPix(): TipoChave {

            return TipoChave.ALEATORIA

        }

        override fun retornaTipoContaPixProto(): com.zup.TipoChave {
                return com.zup.TipoChave.ALEATORIA
        }
    },
    EMAIL {
        override fun retornaTipoContaPix(): TipoChave {

            return TipoChave.EMAIL

        }

        override fun retornaTipoContaPixProto(): com.zup.TipoChave {
            return com.zup.TipoChave.EMAIL
        }
    },

    PHONE {

        override fun retornaTipoContaPix(): TipoChave {

            return TipoChave.TELEFONE

        }

        override fun retornaTipoContaPixProto(): com.zup.TipoChave {
            return com.zup.TipoChave.TELEFONE
        }
    };

    abstract fun retornaTipoContaPix(): TipoChave
    abstract fun retornaTipoContaPixProto(): com.zup.TipoChave

}