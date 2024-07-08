import { renderHook, act } from "@testing-library/react";
import useMission from "../useMission";
import useRefresh from "../useRefresh";
import useMissionJudgment from "../useMissionJudgment";
import { generatePath, useNavigate } from "react-router-dom";
import { PARAM, PATH } from "../../../../constants/path";
import { BUTTON_LABEL } from "../../../../constants/recruitment";
import { Mission } from "../../../../../types/domains/recruitments";

jest.mock("../useRefresh");
jest.mock("../useMissionJudgment");
jest.mock("react-router-dom", () => ({
  generatePath: jest.fn(),
  useNavigate: jest.fn(),
}));

const MOCK_MISSION_ITEM: Mission = {
  id: 1,
  title: "Test Mission",
  description: "Test Description",
  submittable: true,
  submitted: false,
  startDateTime: "2023-01-01T00:00:00",
  endDateTime: "2023-12-31T23:59:59",
  status: "SUBMITTING",
  runnable: true,
  judgment: null,
} as const;

describe("useMission", () => {
  const mockRecruitmentId = "123";

  function createMockMission(overrides = {}) {
    return {
      ...MOCK_MISSION_ITEM,
      ...overrides,
    };
  }

  beforeEach(() => {
    (useRefresh as jest.Mock).mockReturnValue({
      isRefreshAvailable: true,
      fetchRefreshedResultData: jest
        .fn()
        .mockResolvedValue({ ...createMockMission(), title: "Refreshed Mission" }),
    });
    (useMissionJudgment as jest.Mock).mockReturnValue({
      isJudgmentAvailable: true,
      fetchJudgmentMissionResult: jest
        .fn()
        .mockResolvedValue({ ...createMockMission(), title: "Judged Mission" }),
    });
    (useNavigate as jest.Mock).mockReturnValue(jest.fn());
    (generatePath as jest.Mock).mockImplementation((path) => path);
  });

  describe("초기 상태 및 계산된 값을 점검", () => {
    const mockMission = createMockMission();

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
          isJudgmentAvailable: true,
          isRefreshAvailable: true,
        })
      );
    });
  });

  describe("Mission props를 통한 상태 업데이트", () => {
    const mockMission = createMockMission();

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

  describe("라우팅 테스트", () => {
    const mockMission = createMockMission();
    it("미션 과제제출물 미제출 시, 'new' 상태로 과제 제출 페이지로 이동해야 한다", () => {
      const mockNavigate = jest.fn();
      (useNavigate as jest.Mock).mockReturnValue(mockNavigate);

      const { result } = renderHook(() =>
        useMission({ mission: mockMission, recruitmentId: mockRecruitmentId })
      );

      act(() => {
        result.current.routeToAssignmentSubmit({
          recruitmentId: mockRecruitmentId,
          mission: mockMission,
        })();
      });

      expect(generatePath).toHaveBeenCalledWith(PATH.ASSIGNMENT, {
        status: PARAM.ASSIGNMENT_STATUS.NEW,
      });
      expect(mockNavigate).toHaveBeenCalledWith(
        { pathname: PATH.ASSIGNMENT },
        {
          state: {
            recruitmentId: mockRecruitmentId,
            currentMission: mockMission,
          },
        }
      );
    });

    it("미션 과제제출물 제출 시, 'edit' 상태로 과제 제출 페이지로 이동해야 한다", () => {
      const mockNavigate = jest.fn();
      (useNavigate as jest.Mock).mockReturnValue(mockNavigate);

      const submittedMission = { ...mockMission, submitted: true };
      const { result } = renderHook(() =>
        useMission({ mission: submittedMission, recruitmentId: mockRecruitmentId })
      );

      act(() => {
        result.current.routeToAssignmentSubmit({
          recruitmentId: mockRecruitmentId,
          mission: submittedMission,
        })();
      });

      expect(generatePath).toHaveBeenCalledWith(PATH.ASSIGNMENT, {
        status: PARAM.ASSIGNMENT_STATUS.EDIT,
      });
      expect(mockNavigate).toHaveBeenCalledWith(
        { pathname: PATH.ASSIGNMENT },
        {
          state: {
            recruitmentId: mockRecruitmentId,
            currentMission: submittedMission,
          },
        }
      );
    });
  });
  describe("데이터 갱신 테스트", () => {
    describe("requestRefresh 함수 테스트", () => {
      const mockMission = createMockMission();

      it("fetchRefreshedResultData 성공 시 missionItem 상태를 갱신해야 한다", async () => {
        const { result } = renderHook(() =>
          useMission({ mission: mockMission, recruitmentId: mockRecruitmentId })
        );

        await act(async () => {
          await result.current.requestRefresh();
        });

        expect(result.current.getter.missionItem.title).toBe("Refreshed Mission");
      });

      it("fetchRefreshedResultData 실패 시 에러 메시지를 보여줘야 한다.", async () => {
        const errorMessage = "Refresh failed";
        (useRefresh as jest.Mock).mockReturnValue({
          isRefreshAvailable: true,
          fetchRefreshedResultData: jest.fn().mockRejectedValue(new Error(errorMessage)),
        });
        const alertMock = jest.spyOn(window, "alert").mockImplementation(() => {});

        const { result } = renderHook(() =>
          useMission({ mission: mockMission, recruitmentId: mockRecruitmentId })
        );

        await act(async () => {
          await result.current.requestRefresh();
        });

        expect(alertMock).toHaveBeenCalledWith(errorMessage);
        alertMock.mockRestore();
      });
    });

    describe("requestMissionJudgment 함수 테스트", () => {
      const mockMission = createMockMission();

      it("fetchJudgmentMissionResult 성공 시 missionItem 상태를 갱신해야 한다", async () => {
        const { result } = renderHook(() =>
          useMission({ mission: mockMission, recruitmentId: mockRecruitmentId })
        );

        await act(async () => {
          await result.current.requestMissionJudgment();
        });

        expect(result.current.getter.missionItem.title).toBe("Judged Mission");
      });

      it("fetchJudgmentMissionResult 실패 시 에러 메시지를 보여줘야 한다.", async () => {
        const errorMessage = "Judgment failed";
        (useMissionJudgment as jest.Mock).mockReturnValue({
          isJudgmentAvailable: true,
          fetchJudgmentMissionResult: jest.fn().mockRejectedValue(new Error(errorMessage)),
        });
        const alertMock = jest.spyOn(window, "alert").mockImplementation(() => {});

        const { result } = renderHook(() =>
          useMission({ mission: mockMission, recruitmentId: mockRecruitmentId })
        );

        await act(async () => {
          await result.current.requestMissionJudgment();
        });

        expect(alertMock).toHaveBeenCalledWith(errorMessage);
        alertMock.mockRestore();
      });
    });
  });
});
