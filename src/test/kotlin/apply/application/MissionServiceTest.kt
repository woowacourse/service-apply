package apply.application

import apply.createMission
import apply.createMissionData
import apply.domain.mission.MissionRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import support.test.UnitTest

@UnitTest
class MissionServiceTest {
    @MockK
    lateinit var missionRepository: MissionRepository

    private lateinit var missionService: MissionService

    @BeforeEach
    internal fun setUp() {
        missionService = MissionService(missionRepository)
    }

    @Test
    fun `과제를 추가한다`() {
        val missionData = createMissionData()
        every { missionRepository.save(any()) } returns createMission()
        assertDoesNotThrow { missionService.save(missionData) }
    }
}
