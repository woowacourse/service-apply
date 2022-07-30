package apply.application

import apply.domain.cheater.Cheater
import apply.domain.cheater.CheaterRepository
import apply.domain.user.UserRepository
import apply.domain.user.findAllByEmailIn
import apply.domain.user.findByEmail
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class CheaterService(
    private val userRepository: UserRepository,
    private val cheaterRepository: CheaterRepository
) {
    fun save(request: CheaterData): CheaterResponse {
        val email = request.email
        require(!cheaterRepository.existsByEmail(email)) {
            "이미 등록된 부정 행위자입니다."
        }
        val cheater = cheaterRepository.save(Cheater(email, request.description))
        val user = userRepository.findByEmail(email)
        return CheaterResponse(cheater, user)
    }

    fun getById(id: Long): CheaterResponse {
        val cheater = cheaterRepository.findByIdOrNull(id) ?: throw NoSuchElementException()
        return CheaterResponse(cheater, userRepository.findByEmail(cheater.email))
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
