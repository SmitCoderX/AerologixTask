package com.smitcoderx.task.aerologixtask.Utils

import kotlinx.coroutines.flow.*
import java.io.IOException

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(Resources.Loading(data))

        try {
            saveFetchResult(fetch())
            query().map { Resources.Refreshed(it) }
            query().map { Resources.Success(it) }
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> {
                    query().map { Resources.Error("No Internet Connection", it) }
                }
                else -> {
                    query().map { Resources.Error(throwable.localizedMessage, it) }
                }
            }
        }
    } else {
        query().map { Resources.Success(it) }
    }

    emitAll(flow)
}