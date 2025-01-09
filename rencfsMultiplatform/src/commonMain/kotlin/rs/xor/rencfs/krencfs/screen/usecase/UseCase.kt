package rs.xor.rencfs.krencfs.screen.usecase

sealed interface UseCaseParams

fun interface UseCase {
    operator fun invoke()
}

fun interface ParametrizedUseCase<Params : UseCaseParams?> {
    operator fun invoke(params: Params?)
}
