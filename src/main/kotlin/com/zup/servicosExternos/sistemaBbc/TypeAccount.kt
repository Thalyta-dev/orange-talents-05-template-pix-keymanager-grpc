package com.zup.servicosExternos.sistemaBbc

import com.zup.TipoConta

enum class TypeAccount {
    CACC {
        override fun retornaContaProto(): TipoConta {

            return TipoConta.CONTA_POUPANCA
        }


    },
    SVGS {
        override fun retornaContaProto(): TipoConta {
            return TipoConta.CONTA_POUPANCA
        }

    };

    abstract fun retornaContaProto(): TipoConta

}
