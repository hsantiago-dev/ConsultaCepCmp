package br.edu.utfpr.data.remote

import br.edu.utfpr.data.model.EnderecoResponse

interface ViaCepApi {
    suspend fun buscarCep(cep: String): EnderecoResponse
}
