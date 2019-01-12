package net.progruzovik.croco.game

import net.progruzovik.croco.data.Role

interface Player {

    /**
     * id игрока (сессии)
     */
    val id: String

    /**
     * Имя игрока
     */
    var name: String

    /**
     * Роль игрока
     */
    var role: Role

    /**
     * Лобби, в котором находится игрок
     */
    var lobby: Lobby?

    /**
     * Были ли квадраты в текущем лобби перерисованы с момента последнего запроса
     */
    var isQuadsRemoved: Boolean

    /**
     * Ставит игрока в очередь
     *
     * @return Успешность операции
     */
    fun addToQueue(): Boolean

    /**
     * Убирает игрока из очереди
     *
     * @return Успешность операции
     */
    fun removeFromQueue(): Boolean

    /**
     * Добавляет квадрат
     *
     * @param number Номер квадрата
     * @param color Цвет квадрата
     * @return Успешность операции
     */
    fun addQuad(number: Int, color: Int): Boolean

    /**
     * Удаляет квадрат
     *
     * @param number Номер квадрата
     * @return Успешность операции
     */
    fun removeQuad(number: Int): Boolean

    /**
     * Удаляет все квадраты
     *
     * @return Успешность операции
     */
    fun removeQuads(): Boolean

    /**
     * Добавляет сообщение
     *
     * @param text Текст сообщения
     * @return Успешность операции
     */
    fun addMessage(text: String): Boolean

    /**
     * Отмечает сообщение
     *
     * @param number Номер сообщения
     * @param isMarked Вид метки
     * @return Успешность операции
     */
    fun markMessage(number: Int, isMarked: Boolean?): Boolean

    /**
     * Ставит игрока в очередь
     *
     * @return Ключевое слово или null, если у игрока нет доступа нему
     */
    fun requestKeyword(): String?
}
