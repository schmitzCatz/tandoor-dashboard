package net.octosystems.smarthome.tandoordashboard.service

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.*
import org.springframework.util.CollectionUtils
import org.springframework.web.util.UriComponentsBuilder
import java.io.Serializable
import java.net.URI
import java.util.stream.Collectors
import kotlin.reflect.KClass

@DslMarker
annotation class RequestDslMarker

@RequestDslMarker
class RequestBuilder<T : Any> {

    private var headers: HttpHeaders = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
    private var uri: URI? = null
    private var method: HttpMethod = HttpMethod.GET
    private var responseType: KClass<T>? = null
    private var queryParameters: MutableMap<String, String> = mutableMapOf()
    private var contentType: MediaType = MediaType.APPLICATION_JSON

    fun uri(uri: String) = apply { this.uri = URI.create(uri) }
    fun auth(username: String, password: String) = apply { headers.setBasicAuth(username, password) }
    fun token(token: String) = apply { headers.setBearerAuth(token) }
    fun contentType(type: MediaType) = apply { headers.contentType = type }
    fun requestMethod(method: HttpMethod) = apply { this.method = method }
    fun responseType(entity: KClass<T>) = apply { this.responseType = entity }
    fun query(block: QueryBuilder.() -> Unit) = queryParameters.putAll(QueryBuilder().apply(block))

    fun execute(): ResponseEntity<T> {

        if (queryParameters.isNotEmpty()) {
            val parameter = queryParameters.entries.stream()
                .map { it.key to listOf(it.value) }
                .collect(Collectors.toMap({ it.first }, { it.second }))

            this.uri = UriComponentsBuilder.fromUri(uri!!).queryParams(
                CollectionUtils.toMultiValueMap(parameter)
            ).build().toUri()
        }

        return RestTemplateBuilder().build().exchange(uri!!, method, HttpEntity<String>(headers), responseType!!.java)
    }

    @RequestDslMarker
    inner class QueryBuilder(val params: MutableMap<String, String> = mutableMapOf()) :
        MutableMap<String, String> by params {
        fun param(name: String, value: Serializable) {
            params[name] = value.toString()
        }
    }
}


fun <T : Any> request(block: RequestBuilder<T>.() -> Unit): ResponseEntity<T> =
    RequestBuilder<T>().apply(block).execute()