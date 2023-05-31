package com.firstapplication.recipebook.sealed

import androidx.annotation.StringRes

sealed class Response

object ProgressResponse : Response()

class CorrectResponse<T>() : Response() {
    var singleValue: T? = null
    var listValue: List<T>? = null

    constructor(_value: T) : this() {
        singleValue = _value
    }

    constructor(_value: List<T>) : this() {
        listValue = _value
    }
}

class ErrorResponse() : Response() {
    var mesId: Int? = null
        private set

    constructor(@StringRes _mesId: Int) : this() {
        mesId = _mesId
    }
}