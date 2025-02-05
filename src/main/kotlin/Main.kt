package tictactoe

fun main() {
    val input = readln()
    print(
        """
        ---------
        | ${input[0]} ${input[1]} ${input[2]} |
        | ${input[3]} ${input[4]} ${input[5]} |
        | ${input[6]} ${input[7]} ${input[8]} |
        ---------
        """.trimIndent(),
    )
}
