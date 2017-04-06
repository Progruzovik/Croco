package net.progruzovik.study.croco.game

import net.progruzovik.study.croco.dao.KeywordDao
import org.springframework.stereotype.Service

@Service
class QueueService(
        private val keywordDao: KeywordDao) {

    private var queuedPlayer: Player? = null
    private var lastLobby: Lobby? = null

    fun addPlayer(player: Player): Boolean {
        val nextPainter: Player? = queuedPlayer
        if (nextPainter != player) {
            if (lastLobby?.addGuesser(player) != true) {
                if (nextPainter == null) {
                    queuedPlayer = player
                    lastLobby = null
                    player.role = Role.QUEUED
                } else {
                    queuedPlayer = null
                    lastLobby = Lobby(player, nextPainter, keywordDao.getRandomKeyword())
                }
            }
            return true
        }
        return false
    }

    fun removePlayer(player: Player): Boolean {
        if (queuedPlayer == player) {
            queuedPlayer = null
            player.role = Role.IDLER
            return true
        }
        return false
    }
}