package net.progruzovik.study.croco.game

import net.progruzovik.study.croco.data.Role

interface Player {

    /**
     * id игрока (сессии).
     */
    val id: String

    /**
     * Имя игрока.
     */
    var name: String

    /**
     * Роль игрока.
     */
    var role: Role

    /**
     * Лобби, в котором находится игрок.
     */
    var lobby: Lobby?

    /**
     * Были ли квадраты в текущем лобби перерисованы с момента последнего запроса.
     */
    var isQuadsRemoved: Boolean

    /**
     * Ставит игрока в очередь.
     * @return успешность операции.
     */
    fun addToQueue(): Boolean

    /**
     * Убирает игрока из очереди.
     * @return успешность операции.
     */
    fun removeFromQueue(): Boolean

    /**
     * Добавляет квадрат.
     * @param number номер квадрата.
     * @param color цвет квадрата.
     * @return успешность операции.
     */
    fun addQuad(number: Int, color: Int): Boolean

    /**
     * Удаляет квадрат.
     * @param number номер квадрата.
     * @return успешность операции.
     */
    fun removeQuad(number: Int): Boolean

    /**
     * Удаляет все квадраты.
     * @return успешность операции.
     */
    fun removeQuads(): Boolean

    /**
     * Добавляет сообщение.
     * @param text текст сообщения.
     * @return успешность операции.
     */
    fun addMessage(text: String): Boolean

    /**
     * Отмечает сообщение.
     * @param number номер сообщения.
     * @param isMarked вид метки.
     * @return успешность операции.
     */
    fun markMessage(number: Int, isMarked: Boolean?): Boolean

    /**
     * Ставит игрока в очередь.
     * @return ключевое слово или null, если у игрока нет доступа нему.
     */
    fun requestKeyword(): String?
}