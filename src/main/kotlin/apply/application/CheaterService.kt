package apply.application

import apply.domain.cheater.Cheater
import apply.domain.cheater.CheaterRepository
import apply.domain.cheater.getOrThrow
import apply.domain.member.MemberRepository
import apply.domain.member.findAllByEmailIn
import apply.domain.member.findByEmail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class CheaterService(
    private val memberRepository: MemberRepository,
    private val cheaterRepository: CheaterRepository
) {
    fun save(request: CheaterData): CheaterResponse {
        val email = request.email
        require(!cheaterRepository.existsByEmail(email)) {
            "이미 등록된 부정행위자입니다."
        }
        val cheater = cheaterRepository.save(Cheater(email, request.description))
        val member = memberRepository.findByEmail(email)
        return CheaterResponse(cheater, member)
    }

    fun getById(id: Long): CheaterResponse {
        val cheater = cheaterRepository.getOrThrow(id)
        val member = memberRepository.findByEmail(cheater.email)
        return CheaterResponse(cheater, member)
    }

    fun findAll(): List<CheaterResponse> {
        val cheaters = cheaterRepository.findAll()
        val membersByEmail = memberRepository.findAllByEmailIn(cheaters.map { it.email }).associateBy { it.email }
        return cheaters.map { CheaterResponse(it, membersByEmail[it.email]) }
    }

    fun deleteById(id: Long) {
        cheaterRepository.deleteById(id)
    }
}
