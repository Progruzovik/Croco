package net.progruzovik.croco.game

import net.progruzovik.croco.data.Message
import net.progruzovik.croco.data.Quad

interface Lobby {

    /**
     * Художник
     */
    val painter: Player?

    /**
     * Список угадывающих игроков
     */
    val guessers: List<Player>

    /**
     * Победитель
     */
    val winner: Player?

    /**
     * Список нарисованных квадратов
     */
    val quads: List<Quad>

    /**
     * Список сообщений
     */
    val messages: List<Message>

    /**
     * Добавляет угадывающего игрока
     *
     * @param guesser Игрок, который будет добавлен
     * @return Успешность операции
     */
    fun addGuesser(guesser: Player): Boolean

    /**
     * Добавляет квадрат
     *
     * @param player Игрок, который добавляет квадрат
     * @param number Номер квадрата
     * @param color Цвет квадрата
     * @return Успешность операции
     */
    fun addQuad(player: Player, number: Int, color: Int): Boolean

    /**
     * Удаляет квадрат
     *
     * @param player Игрок, который удаляет квадрат
     * @param number Номер квадрата
     * @return Успешность операции
     */
    fun removeQuad(player: Player, number: Int): Boolean

    /**
     * Удаляет все квадраты
     *
     * @param player Игрок, который удаляет квадраты
     * @return Успешность операции
     */
    fun removeQuads(player: Player): Boolean

    /**
     * Добавляет сообщение
     *
     * @param player игрок, который добавляет сообщение
     * @param text Текст сообщения
     * @return Успешность операции
     */
    fun addMessage(player: Player, text: String): Boolean

    /**
     * Отмечает сообщение
     *
     * @param player Игрок, который отмечает сообщение
     * @param number Номер сообщения
     * @param isMarked Вид метки
     * @return Успешность операции
     */
    fun markMessage(player: Player, number: Int, isMarked: Boolean?): Boolean

    /**
     * Заправшивает ключевое слово
     *
     * @param player Игрок, который запрашивает слово
     * @return Ключевое слово или null, если у игрока нет доступа нему
     */
    fun requestKeyword(player: Player): String?

    /**
     * Отправляет запрос на закрытие лобби
     *
     * @param player Игрок, который отправляет запрос
     */
    fun close(player: Player)
}
