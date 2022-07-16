package apply.application

import apply.domain.cheater.Cheater
import apply.domain.cheater.CheaterRepository
import apply.domain.user.UserRepository
import apply.domain.user.findByEmail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class CheaterService(
    private val userRepository: UserRepository,
    private val cheaterRepository: CheaterRepository
) {
    fun findAll(): List<CheaterResponse> = cheaterRepository.findAll().map {
        val user = userRepository.findByEmail(it.email)
        CheaterResponse(it, user)
    }

    fun save(request: CheaterData): Long {
        val email = request.email
        require(!cheaterRepository.existsByEmail(email)) {
            "이미 등록된 부정 행위자입니다."
        }
        val cheater = cheaterRepository.save(Cheater(email, request.description))
        return cheater.id
    }

    fun deleteById(id: Long) {
        cheaterRepository.deleteById(id)
    }
}
