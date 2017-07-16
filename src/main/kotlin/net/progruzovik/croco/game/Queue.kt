package net.progruzovik.croco.game

interface Queue {

    /**
     * Игрок в очереди.
     */
    val queuedPlayer: Player?

    /**
     * Ставит игрока в очередь.
     * @param player игрок, который будет поставлен в очередь.
     * @return успешность операции.
     */
    fun addPlayer(player: Player): Boolean

    /**
     * Убирает игрока из очереди.
     * @param player игрок, который будет убран из очереди.
     * @return успешность операции.
     */
    fun removePlayer(player: Player): Boolean
}