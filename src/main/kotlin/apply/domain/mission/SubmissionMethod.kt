package apply.domain.mission

enum class SubmissionMethod(val label: String) {
    PUBLIC_PULL_REQUEST("공개 풀 리퀘스트"),
    PRIVATE_REPOSITORY("비공개 저장소");
}
