package com.ohapps.snobbinapi.application.exceptionhandlers

import com.ohapps.snobbinapi.domain.common.DataNotFound
import graphql.ErrorType
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.stereotype.Component

@Component
class GraphqlExceptionHandler : DataFetcherExceptionResolverAdapter() {
    override fun resolveToSingleError(ex: Throwable, env: DataFetchingEnvironment): GraphQLError? {
        return when (ex) {
            is DataNotFound -> toDataFetchingException(ex)
            else -> super.resolveToSingleError(ex, env)
        }
    }

    private fun toDataFetchingException(ex: Throwable) = GraphqlErrorBuilder.newError()
        .errorType(ErrorType.DataFetchingException)
        .message(ex.message)
        .build()
}
