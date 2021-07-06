package com.zup.registraChave

enum class TipoChave {

    EMAIL {
        override fun validaChave(chave: String): Boolean {
            return chave.matches("^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\$".toRegex())
        }

    },
    TELEFONE {
        override fun validaChave(chave: String): Boolean {

            return chave.matches("^[0-9]{11}\$".toRegex())

        }

    },
    CPF {
        override fun validaChave(chave: String): Boolean {

            return chave.matches("^([0-9]{3}+-[0-9]{3}+-[0-9]{3}+.[0-9]{2})\$".toRegex())
        }

    },
    ALEATORIA {
        override fun validaChave(chave: String): Boolean {
                return true
        }


    };

    abstract fun validaChave(chave: String): Boolean
}



