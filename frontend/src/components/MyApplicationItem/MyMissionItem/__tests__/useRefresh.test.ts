import { renderHook, act } from "@testing-library/react";
import useRefresh from "../useRefresh";
import { JUDGMENT_STATUS } from "../../../../constants/judgment";
import { MISSION_STATUS } from "../../../../constants/recruitment";
import { isJudgmentTimedOut } from "../../../../utils/validation/judgmentTime";
import useTokenContext from "../../../../hooks/useTokenContext";
import { fetchMyMissionJudgment } from "../../../../api";
import { Mission } from "../../../../../types/domains/recruitments";

jest.mock("../../../../utils/validation/judgmentTime");
jest.mock("../../../../hooks/useTokenContext");
jest.mock("../../../../api");

describe("useRefresh", () => {
  const mockRecruitmentId = 1;
  const mockMissionItem: Mission = {
    id: 1,
    title: "우아한테크코스 99주 차 미션",
    description: "테스트 설명",
    submittable: true,
    submitted: false,
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

  describe("isRefreshAvailable 테스트", () => {
    describe("false를 반환하는 경우", () => {
      it("recruitmentId가 undefined일 때", () => {
        const { result } = renderHook(() =>
          useRefresh({ recruitmentId: undefined, missionItem: mockMissionItem })
        );

        expect(result.current.isRefreshAvailable).toBe(false);
      });

      it("missionItem.id가 없을 때", () => {
        const { result } = renderHook(() =>
          useRefresh({
            recruitmentId: mockRecruitmentId,
            missionItem: { ...mockMissionItem, id: undefined as any },
          })
        );

        expect(result.current.isRefreshAvailable).toBe(false);
      });

      it("missionItem.judgment가 null일 때", () => {
        const { result } = renderHook(() =>
          useRefresh({
            recruitmentId: mockRecruitmentId,
            missionItem: { ...mockMissionItem, judgment: null },
          })
        );

        expect(result.current.isRefreshAvailable).toBe(false);
      });

      it("judgment.status가 STARTED가 아닐 때", () => {
        const { result } = renderHook(() =>
          useRefresh({
            recruitmentId: mockRecruitmentId,
            missionItem: {
              ...mockMissionItem,
              judgment: { ...mockMissionItem.judgment!, status: JUDGMENT_STATUS.SUCCEEDED },
            },
          })
        );

        expect(result.current.isRefreshAvailable).toBe(false);
      });

      it("missionItem.status가 SUBMITTING이 아닐 때", () => {
        const { result } = renderHook(() =>
          useRefresh({
            recruitmentId: mockRecruitmentId,
            missionItem: { ...mockMissionItem, status: MISSION_STATUS.ENDED },
          })
        );

        expect(result.current.isRefreshAvailable).toBe(false);
      });

      it("isJudgmentTimedOut이 true를 반환할 때", () => {
        (isJudgmentTimedOut as jest.Mock).mockReturnValue(true);

        const { result } = renderHook(() =>
          useRefresh({ recruitmentId: mockRecruitmentId, missionItem: mockMissionItem })
        );

        expect(result.current.isRefreshAvailable).toBe(false);
      });
    });

    describe("true를 반환하는 경우", () => {
      it("모든 조건이 충족될 때 true를 반환해야 한다", () => {
        const { result } = renderHook(() =>
          useRefresh({ recruitmentId: mockRecruitmentId, missionItem: mockMissionItem })
        );

        expect(result.current.isRefreshAvailable).toBe(true);
      });
    });
  });

  describe("fetchRefreshedResultData 테스트", () => {
    it("refreshedResultData를 성공적으로 가져와야 한다", async () => {
      const SAMPLE_PASS_COUNT = 8;
      const SAMPLE_TOTAL_COUNT = 10;
      const mockResponse = {
        data: {
          ...mockMissionItem.judgment,
          status: JUDGMENT_STATUS.SUCCEEDED,
          passCount: SAMPLE_PASS_COUNT,
          totalCount: SAMPLE_TOTAL_COUNT,
        },
      };
      (fetchMyMissionJudgment as jest.Mock).mockResolvedValue(mockResponse);

      const { result } = renderHook(() =>
        useRefresh({ recruitmentId: mockRecruitmentId, missionItem: mockMissionItem })
      );

      await act(async () => {
        const refreshedData = await result.current.fetchRefreshedResultData();

        expect(refreshedData).toEqual(
          expect.objectContaining({ ...mockMissionItem, judgment: mockResponse.data })
        );
      });

      expect(fetchMyMissionJudgment).toHaveBeenCalledWith({
        missionId: mockMissionItem.id,
        recruitmentId: mockRecruitmentId,
        token: "mockToken",
      });
    });

    it("refreshedResultData 가져오기 실패 시 에러 메시지를 보여줘야 한다", async () => {
      const errorMessage = "데이터를 가져오는 데 실패했습니다.";
      const mockError = { response: { data: { message: errorMessage } } };
      (fetchMyMissionJudgment as jest.Mock).mockRejectedValue(mockError);

      const { result } = renderHook(() =>
        useRefresh({ recruitmentId: mockRecruitmentId, missionItem: mockMissionItem })
      );

      await expect(result.current.fetchRefreshedResultData()).rejects.toThrow(errorMessage);
    });
  });
});
