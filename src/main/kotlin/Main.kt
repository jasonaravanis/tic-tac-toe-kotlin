package tictactoe

const val BOARD_ROWS = 3
const val BOARD_COLUMNS = 3
const val EMPTY_SPACE = '_'

enum class Player(
    val token: Char,
) {
    X('X'),
    O('O'),
}

fun convertBoardStringToMatrix(board: String): MutableList<MutableList<Char>> {
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

        matrix[row][column] = board[i]
    }

    return matrix
}

fun printBoard(boardMatrix: MutableList<MutableList<Char>>) {
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
    boardMatrix: MutableList<MutableList<Char>>,
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

fun getGameStateFromMatrix(boardMatrix: MutableList<MutableList<Char>>) {
}

fun main() {
    val input = readln()

    val boardMatrix = convertBoardStringToMatrix(input)

    printBoard(boardMatrix)

    val isWinByX = isWinByPlayer(boardMatrix, Player.X)
    val isWinByO = isWinByPlayer(boardMatrix, Player.O)

    println("isWinByX: $isWinByX")
    println("isWinByO: $isWinByO")
}
