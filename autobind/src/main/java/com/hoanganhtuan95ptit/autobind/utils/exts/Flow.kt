package com.hoanganhtuan95ptit.autobind.utils.exts

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun Flow<List<String>>.distinctPattern() = flow {

    val handled = mutableSetOf<String>()

    collect { list ->

        list.filter {
            handled.add(it)
        }.let {

            emit(it)
        }
    }
}