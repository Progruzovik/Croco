package net.progruzovik.croco.game

interface Queue {

    /**
     * Игрок в очереди
     */
    val queuedPlayer: Player?

    /**
     * Ставит игрока в очередь
     *
     * @param player Игрок, который будет поставлен в очередь
     * @return Успешность операции
     */
    fun addPlayer(player: Player): Boolean

    /**
     * Убирает игрока из очереди
     *
     * @param player Игрок, который будет убран из очереди
     * @return Успешность операции
     */
    fun removePlayer(player: Player): Boolean
}
