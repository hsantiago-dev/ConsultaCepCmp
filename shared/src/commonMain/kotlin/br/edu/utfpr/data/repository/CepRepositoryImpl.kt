package br.edu.utfpr.data.repository

import br.edu.utfpr.data.remote.ViaCepApi
import br.edu.utfpr.data.remote.ViaCepApiImpl
import br.edu.utfpr.domain.model.Endereco
import br.edu.utfpr.domain.repository.CepRepository

class CepRepositoryImpl(
    private val api: ViaCepApi
) : CepRepository {

    constructor() : this(ViaCepApiImpl())

    override suspend fun buscarCep(cep: String): Result<Endereco> {
        return runCatching {
            val response = api.buscarCep(cep)
            if (response.erro.equals("true", ignoreCase = true)) {
                throw NoSuchElementException("CEP não encontrado.")
            }
            response.toDomain()
        }
    }
}
