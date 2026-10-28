package br.edu.utfpr.domain.repository

import br.edu.utfpr.domain.model.Endereco

interface CepRepository {
    suspend fun buscarCep(cep: String): Result<Endereco>
}
