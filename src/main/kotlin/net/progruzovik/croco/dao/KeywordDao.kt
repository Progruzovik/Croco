package net.progruzovik.croco.dao

interface KeywordDao {

    /**
     * @return случайное ключевое слово.
     */
    fun getRandomKeyword(): String
}