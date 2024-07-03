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

describe("useMission", () => {
  const mockMission: Mission = {
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
  };

  const mockRecruitmentId = "123";

  beforeEach(() => {
    (useRefresh as jest.Mock).mockReturnValue({
      isRefreshAvailable: true,
      fetchRefreshedResultData: jest
        .fn()
        .mockResolvedValue({ ...mockMission, title: "Refreshed Mission" }),
    });
    (useMissionJudgment as jest.Mock).mockReturnValue({
      isJudgmentAvailable: true,
      fetchJudgmentMissionResult: jest
        .fn()
        .mockResolvedValue({ ...mockMission, title: "Judged Mission" }),
    });
    (useNavigate as jest.Mock).mockReturnValue(jest.fn());
    (generatePath as jest.Mock).mockImplementation((path) => path);
  });

  describe("초기 상태 및 계산된 값을 점검", () => {
    it("missionItem, 버튼 라벨, 포맷된 날짜, 그리고 가용성 플래그를 올바르게 설정해야 한다", () => {
      const { result } = renderHook(() =>
        useMission({ mission: mockMission, recruitmentId: mockRecruitmentId })
      );

      expect(result.current.getter.missionItem).toEqual(mockMission);
      expect(result.current.getter.applyButtonLabel).toBe(BUTTON_LABEL.SUBMIT);
      expect(result.current.getter.formattedStartDateTime).toBe("2023-01-01 00:00");
      expect(result.current.getter.formattedEndDateTime).toBe("2023-12-31 23:59");
      expect(result.current.getter.isJudgmentAvailable).toBe(true);
      expect(result.current.getter.isRefreshAvailable).toBe(true);
    });
  });

  describe("mission prop 변경 시 동작을 테스트", () => {
    it("mission prop이 변경되면 missionItem 상태를 새로운 값으로 업데이트해야 한다", () => {
      const { result, rerender } = renderHook(
        ({ mission }) => useMission({ mission, recruitmentId: mockRecruitmentId }),
        { initialProps: { mission: mockMission } }
      );

      const updatedMission = { ...mockMission, title: "Updated Mission" };
      rerender({ mission: updatedMission });

      expect(result.current.getter.missionItem).toEqual(updatedMission);
    });
  });

  describe("routeToAssignmentSubmit 함수를 테스트", () => {
    it("미션이 제출되지 않았을 때, 'new' 상태로 과제 제출 페이지로 이동해야 한다", () => {
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

    it("미션이 이미 제출되었을 때, 'edit' 상태로 과제 제출 페이지로 이동해야 한다", () => {
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

  describe("requestRefresh 함수 테스트", () => {
    it("fetchRefreshedResultData를 호출하고 그 결과로 missionItem을 업데이트해야 한다", async () => {
      const { result } = renderHook(() =>
        useMission({ mission: mockMission, recruitmentId: mockRecruitmentId })
      );

      await act(async () => {
        await result.current.requestRefresh();
      });

      expect(result.current.getter.missionItem.title).toBe("Refreshed Mission");
    });

    it("fetchRefreshedResultData가 에러를 던지면 에러 메시지를 alert으로 표시해야 한다", async () => {
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

  describe("requestMissionJudgment 함수", () => {
    it("fetchJudgmentMissionResult를 호출하고 그 결과로 missionItem을 업데이트해야 한다", async () => {
      const { result } = renderHook(() =>
        useMission({ mission: mockMission, recruitmentId: mockRecruitmentId })
      );

      await act(async () => {
        await result.current.requestMissionJudgment();
      });

      expect(result.current.getter.missionItem.title).toBe("Judged Mission");
    });

    it("fetchJudgmentMissionResult가 에러를 던지면 에러 메시지를 alert으로 표시해야 한다", async () => {
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
