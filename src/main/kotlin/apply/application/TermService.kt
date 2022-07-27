package apply.application

import apply.domain.recruitment.RecruitmentRepository
import apply.domain.term.Term
import apply.domain.term.TermRepository
import apply.domain.term.getById
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class TermService(
    private val termRepository: TermRepository,
    private val recruitmentRepository: RecruitmentRepository
) {
    fun save(request: TermData): TermResponse {
        check(request.name != Term.SINGLE.name) { "기수명은 ${Term.SINGLE.name}일 수 없습니다." }
        check(!termRepository.existsByName(request.name)) { "이미 등록된 기수명입니다." }
        return termRepository.save(Term(request.name, request.id)).let(::TermResponse)
    }

    fun getById(termId: Long): TermResponse {
        return termRepository.getById(termId).let(::TermResponse)
    }

    fun findAll(): List<TermResponse> {
        val terms = listOf(Term.SINGLE) + termRepository.findAll().sortedBy { it.name }
        return terms.map(::TermResponse)
    }

    fun deleteById(id: Long) {
        check(!recruitmentRepository.existsByTermId(id)) { "모집이 존재하는 기수는 삭제할 수 없습니다." }
        require(termRepository.existsById(id)) { "존재하지 않는 기수는 삭제할 수 없습니다." }
        termRepository.deleteById(id)
    }
}
