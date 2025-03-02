package tictactoe

import kotlin.math.abs

const val BOARD_ROWS = 3
const val BOARD_COLUMNS = 3
const val EMPTY_SPACE = ' '

enum class Player(
    val token: Char,
) {
    X('X'),
    O('O'),
}

typealias BoardMatrix = MutableList<MutableList<Char>>

data class PlayerMove(
    val x: Int,
    val y: Int,
)

fun convertBoardStringToMatrix(board: String): BoardMatrix {
    val matrix =
        MutableList(BOARD_ROWS) { MutableList(BOARD_COLUMNS) { EMPTY_SPACE } }

    for (i in board.indices) {
        var row: Int

        if (i in 0..2) {
            row = 0
        } else if (i in 3..5) {
            row = 1
        } else {
            row = 2
        }

        val column = i % 3

        matrix[row][column] = if (board[i] != '_') board[i] else EMPTY_SPACE
    }

    return matrix
}

fun printBoard(boardMatrix: BoardMatrix) {
    println(
        """
        ---------
        | ${boardMatrix[0][0]} ${boardMatrix[0][1]} ${boardMatrix[0][2]} |
        | ${boardMatrix[1][0]} ${boardMatrix[1][1]} ${boardMatrix[1][2]} |
        | ${boardMatrix[2][0]} ${boardMatrix[2][1]} ${boardMatrix[2][2]} |
        ---------
        """.trimIndent(),
    )
}

fun isWinByPlayer(
    boardMatrix: BoardMatrix,
    player: Player,
): Boolean {
    fun isRowWin(): Boolean {
        var isWin = false
        for (row in boardMatrix) {
            if (row.all { it == player.token }) {
                isWin = true
            }
        }
        return isWin
    }

    fun isColumnWin(): Boolean {
        var isWin = false

        for (columnIndex in 0 until BOARD_COLUMNS) {
            val currentColumn = mutableListOf<Char>()
            for (rowIndex in 0 until BOARD_ROWS) {
                currentColumn.add(boardMatrix[rowIndex][columnIndex])
            }
            if (currentColumn.all { it == player.token }) {
                isWin = true
                break
            } else {
                currentColumn.clear()
            }
        }

        return isWin
    }

    fun isDiagonalWin(): Boolean {
        val northWestToSouthEast = mutableListOf<Char>()
        for (i in 0 until BOARD_COLUMNS) {
            northWestToSouthEast.add(boardMatrix[i][i])
        }

        val southWestToNorthEast = mutableListOf<Char>()
        for (i in 0 until BOARD_COLUMNS) {
            southWestToNorthEast.add(boardMatrix[BOARD_ROWS - 1 - i][i])
        }

        return northWestToSouthEast.all { it == player.token } || southWestToNorthEast.all { it == player.token }
    }

    return isRowWin() || isColumnWin() || isDiagonalWin()
}

fun isBoardFull(boardMatrix: BoardMatrix): Boolean {
    var isComplete = true
    for (row in 0 until BOARD_ROWS) {
        for (column in 0 until BOARD_COLUMNS) {
            if (boardMatrix[row][column] == EMPTY_SPACE) {
                isComplete = false
                break
            }
        }
    }
    return isComplete
}

fun isBoardStateValid(boardMatrix: BoardMatrix): Boolean {
    var playerXMoveCount = 0
    var playerOMoveCount = 0

    for (row in 0 until BOARD_ROWS) {
        for (column in 0 until BOARD_COLUMNS) {
            val playerToken = boardMatrix[row][column]
            if (playerToken == Player.X.token) playerXMoveCount++
            if (playerToken == Player.O.token) playerOMoveCount++
        }
    }

    return abs(playerXMoveCount - playerOMoveCount) < 2
}

fun getPlayerMoveFromInput(input: String): PlayerMove {
    val x: Int
    val y: Int
    try {
        y = input.substring(0, 1).toInt()
        x = input.substring(2, 3).toInt()
    } catch (error: NumberFormatException) {
        throw NumberFormatException("You should enter numbers!")
    }

    if (x !in 1..3 || y !in 1..3) throw IllegalArgumentException("Coordinates should be from 1 to 3!")

    return PlayerMove(x - 1, y - 1)
}

fun applyPlayerMoveToBoard(
    boardMatrix: BoardMatrix,
    playerMove: PlayerMove,
    player: Player,
): BoardMatrix {
    val cellValue = boardMatrix[playerMove.y][playerMove.x]
    if (cellValue != EMPTY_SPACE) throw IllegalArgumentException("This cell is occupied! Choose another one!")
    boardMatrix[playerMove.y][playerMove.x] = player.token
    return boardMatrix
}

fun attemptPlayerTurn(
    boardMatrix: BoardMatrix,
    player: Player,
): BoardMatrix {
    var newBoardMatrix: BoardMatrix? = null
    while (newBoardMatrix == null) {
        try {
            val input = readln()
            val playerMove = getPlayerMoveFromInput(input)
            newBoardMatrix = applyPlayerMoveToBoard(boardMatrix, playerMove, player)
        } catch (exception: Exception) {
            if (exception is NumberFormatException || exception is IllegalArgumentException) {
                println(exception.message)
                continue
            } else {
                throw exception
            }
        }
    }
    return newBoardMatrix
}

fun isGameOver(boardMatrix: BoardMatrix): Boolean {
    val isWinByX = isWinByPlayer(boardMatrix, Player.X)
    val isWinByO = isWinByPlayer(boardMatrix, Player.O)
    val boardFull = isBoardFull(boardMatrix) || isWinByX || isWinByO
    val isDraw = boardFull && !isWinByX && !isWinByO
    val isImpossible = (isWinByX && isWinByO) || !isBoardStateValid(boardMatrix)

    return isWinByX || isWinByO || boardFull || isDraw || isImpossible
}

fun printGameOutcome(boardMatrix: BoardMatrix) {
    val isWinByX = isWinByPlayer(boardMatrix, Player.X)
    val isWinByO = isWinByPlayer(boardMatrix, Player.O)
    val boardFull = isBoardFull(boardMatrix) || isWinByX || isWinByO
    val isDraw = boardFull && !isWinByX && !isWinByO
    val isImpossible = (isWinByX && isWinByO) || !isBoardStateValid(boardMatrix)
    println(
        when {
            isImpossible -> "Impossible"
            !boardFull -> "Game not finished"
            isDraw -> "Draw"
            isWinByX -> "X wins"
            else -> "O wins"
        },
    )
}

fun rotateCurrentPlayer(player: Player): Player = if (player == Player.X) Player.O else Player.X

fun main() {
    val initialGameState = "_________"
    var boardMatrix = convertBoardStringToMatrix(initialGameState)
    printBoard(boardMatrix)

    var currentPlayer = Player.X
    var gameOver = false

    while (!gameOver) {
        boardMatrix = attemptPlayerTurn(boardMatrix, currentPlayer)
        printBoard(boardMatrix)
        gameOver = isGameOver(boardMatrix)
        if (gameOver) printGameOutcome(boardMatrix) else currentPlayer = rotateCurrentPlayer(currentPlayer)
    }
}
