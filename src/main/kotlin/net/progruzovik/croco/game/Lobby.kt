package net.progruzovik.croco.game

import net.progruzovik.croco.data.Message
import net.progruzovik.croco.data.Quad

interface Lobby {

    /**
     * Художник.
     */
    val painter: Player?

    /**
     * Список угадывающих игроков.
     */
    val guessers: List<Player>

    /**
     * Победитель.
     */
    val winner: Player?

    /**
     * Список нарисованных квадратов.
     */
    val quads: List<Quad>

    /**
     * Список сообщений.
     */
    val messages: List<Message>

    /**
     * Добавляет угадывающего игрока.
     * @param guesser игрок, который будет добавлен.
     * @return успешность операции.
     */
    fun addGuesser(guesser: Player): Boolean

    /**
     * Добавляет квадрат.
     * @param player игрок, который добавляет квадрат.
     * @param number номер квадрата.
     * @param color цвет квадрата.
     * @return успешность операции.
     */
    fun addQuad(player: Player, number: Int, color: Int): Boolean

    /**
     * Удаляет квадрат.
     * @param player игрок, который удаляет квадрат.
     * @param number номер квадрата.
     * @return успешность операции.
     */
    fun removeQuad(player: Player, number: Int): Boolean

    /**
     * Удаляет все квадраты.
     * @param player игрок, который удаляет квадраты.
     * @return успешность операции.
     */
    fun removeQuads(player: Player): Boolean

    /**
     * Добавляет сообщение.
     * @param player игрок, который добавляет сообщение.
     * @param text текст сообщения.
     * @return успешность операции.
     */
    fun addMessage(player: Player, text: String): Boolean

    /**
     * Отмечает сообщение.
     * @param player игрок, который отмечает сообщение.
     * @param number номер сообщения.
     * @param isMarked вид метки.
     * @return успешность операции.
     */
    fun markMessage(player: Player, number: Int, isMarked: Boolean?): Boolean

    /**
     * Заправшивает ключевое слово.
     * @param player игрок, который запрашивает слово.
     * @return ключевое слово или null, если у игрока нет доступа нему.
     */
    fun requestKeyword(player: Player): String?

    /**
     * Отправляет запрос на закрытие лобби.
     * @param player игрок, который отправляет запрос.
     */
    fun close(player: Player)
}