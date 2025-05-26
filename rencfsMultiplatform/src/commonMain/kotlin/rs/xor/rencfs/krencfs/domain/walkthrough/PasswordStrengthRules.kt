package rs.xor.rencfs.krencfs.domain.walkthrough

object PasswordStrengthConstants {
    const val MINIMUM_LENGTH_WEAK = 8
    const val MINIMUM_LENGTH_STRONG = 12
}

object PasswordStrengthRegex {
    val LETTERS = Regex("[a-zA-Z]")
    val DIGITS = Regex("[0-9]")
    val SPECIAL_CHARS = Regex("[^a-zA-Z0-9]")
}

fun calculatePasswordStrength(password: String): PasswordStrength {
    if (password.length < PasswordStrengthConstants.MINIMUM_LENGTH_WEAK) {
        return PasswordStrength.WEAK
    }

    val hasLetters = password.contains(PasswordStrengthRegex.LETTERS)
    val hasDigits = password.contains(PasswordStrengthRegex.DIGITS)
    val hasSpecialChars = password.contains(PasswordStrengthRegex.SPECIAL_CHARS)

    return when {
        password.length >= PasswordStrengthConstants.MINIMUM_LENGTH_STRONG &&
            hasLetters &&
            hasDigits &&
            hasSpecialChars -> PasswordStrength.STRONG
        hasLetters && hasDigits -> PasswordStrength.MEDIUM
        else -> PasswordStrength.WEAK
    }
}
