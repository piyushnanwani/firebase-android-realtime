package com.piyushnanwani.mynovemberapp


data class NewsArticle(
    val title: String= "",
    val content: String = "",
    val author: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
