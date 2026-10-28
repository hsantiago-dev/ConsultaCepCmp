package br.edu.utfpr.data.model

import br.edu.utfpr.domain.model.Endereco
import kotlinx.serialization.Serializable

@Serializable
data class EnderecoResponse(
    val cep: String? = null,
    val logradouro: String? = null,
    val bairro: String? = null,
    val localidade: String? = null,
    val uf: String? = null,
    val erro: String? = null
) {
    fun toDomain(): Endereco {
        return Endereco(
            cep = cep.orEmpty(),
            logradouro = logradouro.orEmpty(),
            bairro = bairro.orEmpty(),
            localidade = localidade.orEmpty(),
            uf = uf.orEmpty()
        )
    }
}
