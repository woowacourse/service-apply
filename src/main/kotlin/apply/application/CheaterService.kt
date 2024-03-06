package apply.application

import apply.domain.cheater.Cheater
import apply.domain.cheater.CheaterRepository
import apply.domain.cheater.getOrThrow
import apply.domain.user.MemberRepository
import apply.domain.user.findAllByEmailIn
import apply.domain.user.findByEmail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class CheaterService(
    private val userRepository: MemberRepository,
    private val cheaterRepository: CheaterRepository
) {
    fun save(request: CheaterData): CheaterResponse {
        val email = request.email
        require(!cheaterRepository.existsByEmail(email)) {
            "이미 등록된 부정행위자입니다."
        }
        val cheater = cheaterRepository.save(Cheater(email, request.description))
        val user = userRepository.findByEmail(email)
        return CheaterResponse(cheater, user)
    }

    fun getById(id: Long): CheaterResponse {
        val cheater = cheaterRepository.getOrThrow(id)
        val user = userRepository.findByEmail(cheater.email)
        return CheaterResponse(cheater, user)
    }

    fun findAll(): List<CheaterResponse> {
        val cheaters = cheaterRepository.findAll()
        val usersByEmail = userRepository.findAllByEmailIn(cheaters.map { it.email }).associateBy { it.email }
        return cheaters.map { CheaterResponse(it, usersByEmail[it.email]) }
    }

    fun deleteById(id: Long) {
        cheaterRepository.deleteById(id)
    }
}
