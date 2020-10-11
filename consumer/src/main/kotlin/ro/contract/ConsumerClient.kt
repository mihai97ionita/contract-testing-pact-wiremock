package ro.contract

import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import io.reactivex.Single

@Client("\${consumer.client.host}:\${consumer.client.port}")
interface ConsumerClient{

    @Get("/get")
    fun get(@QueryValue id: String): Single<ResponseDTO>
}