package net.progruzovik.croco.dao

interface KeywordDao {

    /**
     * @return Случайное ключевое слово
     */
    fun getRandomKeyword(): String
}
