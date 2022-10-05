package com.firstapplication.recipebook.sealed

sealed class Error {
    object CorrectResult : Error()
    object ErrorResult : Error()
}