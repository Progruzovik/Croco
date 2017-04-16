package net.progruzovik.study.croco.dao

interface KeywordDao {

    /**
     * @return случайное ключевое слово.
     */
    fun getRandomKeyword(): String
}