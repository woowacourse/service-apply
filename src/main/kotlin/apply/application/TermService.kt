package apply.application

import apply.domain.recruitment.RecruitmentRepository
import apply.domain.term.Term
import apply.domain.term.TermRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@Transactional
@Service
class TermService(
    private val termRepository: TermRepository,
    private val recruitmentRepository: RecruitmentRepository
) {
    @PostConstruct
    private fun populateDummy() {
        if (termRepository.count() != 0L) {
            return
        }
        val terms = listOf(
            Term("1기"),
            Term("2기"),
            Term("3기"),
            Term("4기")
        )
        termRepository.saveAll(terms)
    }

    fun save(request: TermData) {
        check(request.name != Term.SINGLE.name) { "기수명은 ${Term.SINGLE.name}일 수 없습니다." }
        termRepository.save(request.toEntity())
    }

    fun deleteById(id: Long) {
        check(!recruitmentRepository.existsByTermId(id)) { "모집이 존재하는 기수는 삭제할 수 없습니다." }
        check(termRepository.existsById(id)) { "존재하지 않는 기수는 삭제할 수 없습니다." }
        termRepository.deleteById(id)
    }
}
