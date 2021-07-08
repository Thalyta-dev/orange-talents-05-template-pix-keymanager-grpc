package com.zup.registraChave

import com.zup.servicosExternos.sistemaBbc.TypeAccount

enum class TipoConta {
    CONTA_CORRENTE {
        override fun returnTipoContaBbc(): TypeAccount {
            return TypeAccount.CACC
        }

    },
    CONTA_POUPANCA {
        override fun returnTipoContaBbc(): TypeAccount {
            return TypeAccount.SVGS
        }

    };

    abstract fun returnTipoContaBbc() : TypeAccount
}
