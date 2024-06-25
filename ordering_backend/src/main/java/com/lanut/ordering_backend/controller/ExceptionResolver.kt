package com.lanut.ordering_backend.controller

import com.lanut.ordering_backend.entity.vo.RestFailure
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.NoHandlerFoundException

@RestControllerAdvice
class ExceptionResolver {
    @ExceptionHandler(NoHandlerFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNoHandlerFound(e: NoHandlerFoundException, request: WebRequest?): String {
        val localizedMessage = e.localizedMessage
        return localizedMessage.RestFailure(404)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(e: Exception, request: WebRequest?): String {
        val localizedMessage = e.localizedMessage
        return localizedMessage.RestFailure(500)
    }

    @ExceptionHandler(NotImplementedError::class)
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    fun handleNotImplementedError(e: NotImplementedError, request: WebRequest?): String {
        val localizedMessage = e.localizedMessage
        return localizedMessage.RestFailure(501)
    }
}

