import { renderHook, act } from "@testing-library/react";
import useMission from "../useMission";
import useRefresh from "../useRefresh";
import useMissionJudgment from "../useMissionJudgment";
import { generatePath, useNavigate } from "react-router-dom";
import { PARAM, PATH } from "../../../../constants/path";
import { BUTTON_LABEL } from "../../../../constants/recruitment";
import { createMockMission } from "./testMissionMockUtils";

jest.mock("../useRefresh");
jest.mock("../useMissionJudgment");
jest.mock("react-router-dom", () => ({
  generatePath: jest.fn(),
  useNavigate: jest.fn(),
}));

describe("useMission", () => {
  const mockRecruitmentId = "123";

  function createMissionItem(overrides = {}) {
    return createMockMission({
      title: "Test Mission",
      description: "Test Description",
      ...overrides,
    });
  }

  beforeEach(() => {
    (useRefresh as jest.Mock).mockReturnValue({
      isRefreshAvailable: true,
      fetchRefreshedResultData: jest
        .fn()
        .mockResolvedValue({ ...createMissionItem(), title: "Refreshed Mission" }),
    });
    (useMissionJudgment as jest.Mock).mockReturnValue({
      isJudgmentAvailable: true,
      fetchJudgmentMissionResult: jest
        .fn()
        .mockResolvedValue({ ...createMissionItem(), title: "Judged Mission" }),
    });
    (useNavigate as jest.Mock).mockReturnValue(jest.fn());
    (generatePath as jest.Mock).mockImplementation((path) => path);
  });

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
          isJudgmentAvailable: true,
          isRefreshAvailable: true,
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

  describe("라우팅 테스트", () => {
    const mockMission = createMissionItem();
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
      const mockMission = createMissionItem();

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
      const mockMission = createMissionItem();

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
