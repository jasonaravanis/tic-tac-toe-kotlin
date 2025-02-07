package tictactoe

fun convertBoardStringToMatrix(board: String): MutableList<MutableList<Char>> {
    val matrix =
        mutableListOf(
            mutableListOf<Char>('_', '_', '_'),
            mutableListOf<Char>('_', '_', '_'),
            mutableListOf<Char>('_', '_', '_'),
        )

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
    print(
        """
        ---------
        | ${boardMatrix[0][0]} ${boardMatrix[0][1]} ${boardMatrix[0][2]} |
        | ${boardMatrix[1][0]} ${boardMatrix[1][1]} ${boardMatrix[1][2]} |
        | ${boardMatrix[2][0]} ${boardMatrix[2][1]} ${boardMatrix[2][2]} |
        ---------
        """.trimIndent(),
    )
}

enum class Player(
    val token: Char,
) {
    X('X'),
    O('O'),
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

//    fun isColumnWin(): Boolean {
//    }
//
//    fun isDiagonalWin(): Boolean {
//    }

//    return isRowWin() || isColumnWin() || isDiagonalWin()
    return isRowWin()
}

fun getGameStateFromMatrix(boardMatrix: MutableList<MutableList<Char>>) {
}

fun main() {
    val input = readln()

    val boardMatrix = convertBoardStringToMatrix(input)

    printBoard(boardMatrix)

    val isRowWinByX = isWinByPlayer(boardMatrix, Player.X)

    println("Is row win by X: $isRowWinByX")
}
