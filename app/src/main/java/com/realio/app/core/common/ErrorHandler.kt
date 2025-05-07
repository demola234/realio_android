package com.realio.app.core.common

class ErrorHandler<T, Boolean, E: Exception>  {
    var data: T? = null
    var loading: kotlin.Boolean? = null
    var e: E? = null
    constructor(
        data: T?,
        loading: kotlin.Boolean?,
        e: E?
    ) {
        this.data = data
        this.loading = loading
        this.e = e
    }
}