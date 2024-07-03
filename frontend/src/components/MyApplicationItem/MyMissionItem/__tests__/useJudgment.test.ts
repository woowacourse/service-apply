import { renderHook, act } from "@testing-library/react";
import useMissionJudgment from "../useMissionJudgment";
import { postMyMissionJudgment } from "../../../../api";
import useTokenContext from "../../../../hooks/useTokenContext";
import { isJudgmentTimedOut } from "../../../../utils/validation/judgmentTime";
import { MISSION_STATUS } from "../../../../constants/recruitment";
import { JUDGMENT_STATUS } from "../../../../constants/judgment";
import { Mission } from "../../../../../types/domains/recruitments";

jest.mock("../../../../api");
jest.mock("../../../../hooks/useTokenContext");
jest.mock("../../../../utils/validation/judgmentTime");

describe("useMissionJudgment 훅 테스트", () => {
  const mockRecruitmentId = 1;
  const mockMissionItem: Mission = {
    id: 1,
    title: "테스트 미션",
    description: "테스트 설명",
    submittable: true,
    submitted: true,
    startDateTime: "2023-01-01T00:00:00",
    endDateTime: "2023-12-31T23:59:59",
    status: MISSION_STATUS.SUBMITTING,
    runnable: true,
    judgment: {
      pullRequestUrl: "https://github.com/test/pr",
      commitHash: "abcdef1234567890",
      status: JUDGMENT_STATUS.STARTED,
      passCount: 0,
      totalCount: 10,
      message: "",
      startedDateTime: "2023-06-01T12:00:00",
      commitUrl: "https://github.com/test/commit",
    },
  };

  beforeEach(() => {
    (useTokenContext as jest.Mock).mockReturnValue({ token: "mockToken" });
    (isJudgmentTimedOut as jest.Mock).mockReturnValue(false);
  });

  describe("isJudgmentAvailable 테스트", () => {
    it("미션이 제출되지 않았고 제출 기간 전인 경우 isJudgmentAvailable이 false를 반환해야 한다.", () => {
      const unsubmittedMissionItem = {
        ...mockMissionItem,
        submitted: false,
        status: MISSION_STATUS.SUBMITTABLE,
      };
      const { result } = renderHook(() =>
        useMissionJudgment({
          missionItem: unsubmittedMissionItem,
          recruitmentId: mockRecruitmentId,
        })
      );

      expect(result.current.isJudgmentAvailable).toBe(false);
    });

    it("미션이 제출되지 않았을 때 isJudgmentAvailable이 false를 반환해야 한다", () => {
      const unsubmittedMissionItem = { ...mockMissionItem, submitted: false };
      const { result } = renderHook(() =>
        useMissionJudgment({
          missionItem: unsubmittedMissionItem,
          recruitmentId: mockRecruitmentId,
        })
      );

      expect(result.current.isJudgmentAvailable).toBe(false);
    });

    it("미션이 제출되었고, 예제 테스트 채점이 시작되지 않았을 때 isJudgmentAvailable이 true를 반환해야 한다", () => {
      const missionWithoutJudgment = { ...mockMissionItem, judgment: null };
      const { result } = renderHook(() =>
        useMissionJudgment({
          missionItem: missionWithoutJudgment,
          recruitmentId: mockRecruitmentId,
        })
      );

      expect(result.current.isJudgmentAvailable).toBe(true);
    });

    it("미션이 제출되었고, 예제 테스트 채점이 시작되었을 때 isJudgmentAvailable이 true를 반환해야 한다", () => {
      (isJudgmentTimedOut as jest.Mock).mockReturnValue(true);
      const { result } = renderHook(() =>
        useMissionJudgment({ missionItem: mockMissionItem, recruitmentId: mockRecruitmentId })
      );

      expect(result.current.isJudgmentAvailable).toBe(true);
    });

    it("미션 상태가 제출 기간 이후 시점(ENDED)일 때 isJudgmentAvailable이 false를 반환해야 한다", () => {
      const endedMission = { ...mockMissionItem, status: MISSION_STATUS.ENDED };
      const { result } = renderHook(() =>
        useMissionJudgment({ missionItem: endedMission, recruitmentId: mockRecruitmentId })
      );

      expect(result.current.isJudgmentAvailable).toBe(false);
    });

    it("미션 상태가 제출 기간 사이에 제출이 불가능한 경우(UNSUBMITTABLE)일 때 isJudgmentAvailable이 false를 반환해야 한다", () => {
      const unsubmittableMission = { ...mockMissionItem, status: MISSION_STATUS.UNSUBMITTABLE };
      const { result } = renderHook(() =>
        useMissionJudgment({ missionItem: unsubmittableMission, recruitmentId: mockRecruitmentId })
      );

      expect(result.current.isJudgmentAvailable).toBe(false);
    });
  });

  describe("fetchJudgmentMissionResult 테스트", () => {
    it("판정 결과를 성공적으로 가져와야 한다", async () => {
      const mockResponse = {
        data: {
          ...mockMissionItem.judgment,
          status: JUDGMENT_STATUS.SUCCEEDED,
          passCount: 8,
          totalCount: 10,
        },
      };
      (postMyMissionJudgment as jest.Mock).mockResolvedValue(mockResponse);

      const { result } = renderHook(() =>
        useMissionJudgment({ missionItem: mockMissionItem, recruitmentId: mockRecruitmentId })
      );

      await act(async () => {
        const judgmentResult = await result.current.fetchJudgmentMissionResult();
        expect(judgmentResult).toEqual({ ...mockMissionItem, judgment: mockResponse.data });
        expect(judgmentResult.judgment?.status).toBe(JUDGMENT_STATUS.SUCCEEDED);
        expect(judgmentResult.judgment?.passCount).toBe(8);
        expect(judgmentResult.judgment?.totalCount).toBe(10);
      });

      expect(postMyMissionJudgment).toHaveBeenCalledWith({
        missionId: mockMissionItem.id,
        recruitmentId: mockRecruitmentId,
        token: "mockToken",
      });
    });

    it("판정 결과 가져오기 실패 시 에러를 던져야 한다", async () => {
      const errorMessage = "판정 결과를 가져오는 데 실패했습니다.";
      const mockError = { response: { data: { message: errorMessage } } };
      (postMyMissionJudgment as jest.Mock).mockRejectedValue(mockError);

      const { result } = renderHook(() =>
        useMissionJudgment({ missionItem: mockMissionItem, recruitmentId: mockRecruitmentId })
      );

      await expect(result.current.fetchJudgmentMissionResult()).rejects.toThrow(errorMessage);
    });
  });
});
