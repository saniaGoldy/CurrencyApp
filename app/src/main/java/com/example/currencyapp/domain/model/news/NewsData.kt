package com.example.currencyapp.domain.model.news

data class NewsData(

    val description: String,

    val publishedAt: PublishDate,

    val source: String,

    val tags: List<String>,

    val title: String,

    val url: String
){
    fun containsKeyword(keyword: String, ignoreCase: Boolean = true): Boolean{
        return description.contains(keyword, ignoreCase)
                || publishedAt.publishDate.contains(keyword, ignoreCase)
                || publishedAt.publishTime.contains(keyword, ignoreCase)
                || source.contains(keyword, ignoreCase)
                || tags.firstNotNullOfOrNull { it.contains(keyword, ignoreCase) } ?: false
                || title.contains(keyword, ignoreCase)
    }

    fun getTagsAsString(): String{
        return tags.toString().let { it.substring(1, it.length) }
    }
}
