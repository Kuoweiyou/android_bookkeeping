package com.kuo.bookkeeping.extension

fun String.trimEndZero(): String {
    return this.trimEnd { it == '0' }.trimEnd { it == '.' }.trimEnd { it == ',' }
}