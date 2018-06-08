package passocerto.com.br.passocerto

import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.Retrofit.*
import retrofit2.http.*


interface UsuarioREST {

    @POST("api/usuarios")
    fun inserirUsuario(@Body usuario: Usuario): Call<Void>

    @GET("api/usuarios")
    fun getUsuario(): Call<List<Usuario>>

    @GET("api/usuarios/{id}")
    fun getUsuarioPorId(@Path("id") id: String): Call<Usuario>

    @GET("api/usuarios/{login}/{senha}")
    fun getUsuarioPorLogin(@Path("login") login: String, @Path("senha") senha: String): Call<Usuario>


    @PUT("api/usuarios/{id}")
    fun alterarUsuario(@Path("id") id: String, @Body usuario: Usuario): Call<Void>

    @DELETE("api/usuarios/{id}")
    fun removerUsuario(@Path("id") id: Int?): Call<Void>
}

