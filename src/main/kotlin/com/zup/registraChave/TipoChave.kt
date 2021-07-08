package com.zup.registraChave

import com.zup.servicosExternos.sistemaBbc.KeyType

enum class TipoChave {

    EMAIL {
        override fun validaChave(chave: String): Boolean {
            return chave.matches("^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\$".toRegex())
        }

        override fun retornaChaveBbc(): KeyType {
            return KeyType.EMAIL
        }

    },
    TELEFONE {
        override fun validaChave(chave: String): Boolean {

            return chave.matches("^[0-9]{11}\$".toRegex())

        }

        override fun retornaChaveBbc(): KeyType {
            return KeyType.PHONE
        }

    },
    CPF {
        override fun validaChave(chave: String): Boolean {

            return chave.matches("^([0-9]{3}+-[0-9]{3}+-[0-9]{3}+.[0-9]{2})\$".toRegex())
        }

        override fun retornaChaveBbc(): KeyType {
            return KeyType.CPF
        }

    },
    ALEATORIA {
        override fun validaChave(chave: String): Boolean {
                return true
        }

        override fun retornaChaveBbc(): KeyType {
            return KeyType.RANDOM
        }


    };

    abstract fun validaChave(chave: String): Boolean

    abstract fun retornaChaveBbc(): KeyType

}



