import { renderHook } from "@testing-library/react";
import useMission from "../../../../hooks/useMission";
import { BUTTON_LABEL } from "../../../../constants/recruitment";
import { createMockMission } from "./testMissionMockUtils";

jest.mock("react-router-dom", () => ({
  generatePath: jest.fn(),
  useNavigate: jest.fn(),
}));

describe("useMission 훅 테스트", () => {
  const mockRecruitmentId = "123";

  function createMissionItem(overrides = {}) {
    return createMockMission({
      title: "Test Mission",
      description: "Test Description",
      ...overrides,
    });
  }

  describe("초기 상태 및 계산된 값을 점검", () => {
    const mockMission = createMissionItem();

    it("missionItem, 버튼 레이블, 날짜, flag 값을 정해진 형식대로 설정해야 한다", () => {
      const { result } = renderHook(() =>
        useMission({ mission: mockMission, recruitmentId: mockRecruitmentId })
      );

      expect(result.current.getter).toEqual(
        expect.objectContaining({
          missionItem: expect.objectContaining({ ...mockMission }),
          applyButtonLabel: BUTTON_LABEL.SUBMIT,
          formattedStartDateTime: "2023-01-01 00:00",
          formattedEndDateTime: "2023-12-31 23:59",
        })
      );
    });
  });

  describe("Mission props를 통한 상태 업데이트", () => {
    const mockMission = createMissionItem();

    it("missionItem 상태를 새로운 값으로 갱신해야 한다", () => {
      const { result, rerender } = renderHook(
        ({ mission }) => useMission({ mission, recruitmentId: mockRecruitmentId }),
        { initialProps: { mission: mockMission } }
      );

      const updatedMission = { ...mockMission, title: "Updated Mission" };
      rerender({ mission: updatedMission });

      expect(result.current.getter.missionItem).toEqual(updatedMission);
    });
  });
});
