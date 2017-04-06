package net.progruzovik.study.croco.dao

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
open class KeywordJdbc(
        private val jdbcTemplate: JdbcTemplate) : KeywordDao {

    override fun getRandomKeyword(): String {
        return jdbcTemplate.queryForObject("SELECT value FROM keyword ORDER BY RAND() LIMIT 1", String::class.java)
    }
}