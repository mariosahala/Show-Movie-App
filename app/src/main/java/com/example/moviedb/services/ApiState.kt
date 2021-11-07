package com.example.moviedb.services

import com.example.moviedb.util.State

data class ApiState<out T>(val status: State, val data: T?, val message: String?, val code: Int?) {

    companion object {

        fun <T> success(data: T?): ApiState<T> {
            return ApiState(State.SUCCESS, data, null, 200)
        }

        fun <T> error(msg: String, data: T?, code: Int?): ApiState<T> {
            return ApiState(State.ERROR, data, msg, code)
        }

        fun <T> loading(): ApiState<T> {
            return ApiState(State.LOADING, null, null, 200)
        }

    }

}
