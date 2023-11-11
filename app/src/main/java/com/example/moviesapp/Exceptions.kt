package com.example.moviesapp

const val NOT_Null = "can not be null"
const val PAGINATION = "PaginationScrollListener"

fun illegalState(cause: String): Nothing = throw IllegalStateException(cause)

fun nullQueryParameters(): Nothing = illegalState("$PAGINATION queryParameters $NOT_Null")

fun nullLifeCycleOwner(): Nothing = illegalState("$PAGINATION lifeCycleOwner $NOT_Null")

fun nullLayoutManager(): Nothing = illegalState("$PAGINATION layoutManager $NOT_Null")

fun nullRetrieve(): Nothing = illegalState("$PAGINATION retrieve must have implementation")