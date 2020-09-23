package apply.application

import apply.domain.recruitmentitem.RecruitmentItem
import apply.domain.recruitmentitem.RecruitmentItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@Transactional
@Service
class RecruitmentItemService(private val recruitmentItemItemRepository: RecruitmentItemRepository) {
    fun findByRecruitmentIdOrderByPosition(recruitmentId: Long): List<RecruitmentItem> {
        return recruitmentItemItemRepository.findByRecruitmentIdOrderByPosition(recruitmentId)
    }

    @PostConstruct
    private fun populateDummy() {
        if (recruitmentItemItemRepository.count() != 0L) {
            return
        }
        val recruitmentItems = listOf(
            RecruitmentItem(
                title = "프로그래머가 되려는 이유는 무엇인가요?",
                description = "어떤 계기로 프로그래머라는 직업을 꿈꾸게 되었나요? 프로그래밍을 배워 최종적으로 하고 싶은 일이 무엇인지, 프로그래밍을 통해 만들고 싶은 소프트웨어가 있다면 무엇인지에 대해 작성해 주세요.",
                recruitmentId = 1L,
                maximumLength = 1000,
                position = 1
            ),
            RecruitmentItem(
                title = "프로그래밍 학습 과정과 현재 자신이 생각하는 역량은",
                description = "우아한테크코스는 프로그래밍에 대한 기본 지식과 경험을 가진 교육생을 선발하기 때문에 프로그래밍 경험이 있는 상태에서 지원하게 됩니다. 프로그래밍 학습을 어떤 계기로 시작했으며, 어떻게 학습해왔는지, 이를 통해 현재 어느 정도의 역량을 보유한 상태인지를 구체적으로 작성해 주세요.",
                recruitmentId = 1L,
                maximumLength = 1000,
                position = 0
            )
        )
        recruitmentItemItemRepository.saveAll(recruitmentItems)
    }
}
