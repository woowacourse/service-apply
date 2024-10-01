import { renderHook, act } from "@testing-library/react";
import useMissionJudgment from "../../../../hooks/useMissionJudgment";
import { postMyMissionJudgment } from "../../../../api";
import useTokenContext from "../../../../hooks/useTokenContext";
import { isJudgmentTimedOut } from "../../../../utils/validation/judgmentTime";
import { MISSION_STATUS } from "../../../../constants/recruitment";
import { JUDGMENT_STATUS } from "../../../../constants/judgment";
import { Mission } from "../../../../../types/domains/recruitments";
import { createMockJudgment, createMockMission } from "./testMissionMockUtils";

jest.mock("../../../../api");
jest.mock("../../../../hooks/useTokenContext");
jest.mock("../../../../utils/validation/judgmentTime");

describe("useMissionJudgment 훅 테스트", () => {
  const mockRecruitmentId = 1;

  function createMissionItem(overrides = {}) {
    return createMockMission({
      submitted: true,
      judgment: createMockJudgment(),
      ...overrides,
    });
  }

  function renderUseMissionJudgment(
    missionItem = createMockMission(),
    recruitmentId = mockRecruitmentId
  ) {
    return renderHook(() => useMissionJudgment({ missionItem, recruitmentId }));
  }

  function expectJudgmentAvailability(missionItem: Partial<Mission>, expected: boolean) {
    const { result } = renderUseMissionJudgment({
      ...createMissionItem(),
      ...missionItem,
    } as Mission);
    expect(result.current.isJudgmentAvailable).toBe(expected);
  }

  beforeEach(() => {
    (useTokenContext as jest.Mock).mockReturnValue({ token: "mockToken" });
    (isJudgmentTimedOut as jest.Mock).mockReturnValue(false);
  });

  describe("isJudgmentAvailable 테스트", () => {
    describe("사용자가 미션 과제 제출물을 제출하지 않은 경우", () => {
      it("제출 기간 전일 때 false를 반환해야 한다", () => {
        expectJudgmentAvailability({ submitted: false, status: MISSION_STATUS.SUBMITTABLE }, false);
      });

      it("제출 기간 중 false를 반환해야 한다", () => {
        expectJudgmentAvailability({ submitted: false }, false);
      });
    });

    describe("사용자가 미션 과제 제출물을 제출한 경우", () => {
      it("예제 테스트 채점이 시작되지 않았을 때 true를 반환해야 한다", () => {
        expectJudgmentAvailability({ judgment: null }, true);
      });

      it("예제 테스트 채점이 시작되었을 때 true를 반환해야 한다", () => {
        (isJudgmentTimedOut as jest.Mock).mockReturnValue(true);
        expectJudgmentAvailability({}, true);
      });
    });

    describe("미션 상태에 따른 채점 요청 가능 여부", () => {
      it("미션 상태가 제출 기간 이후 시점(ENDED)일 때 false를 반환해야 한다", () => {
        expectJudgmentAvailability({ status: MISSION_STATUS.ENDED }, false);
      });

      it("미션 상태가 제출 기간 사이에 제출이 불가능한 경우(UNSUBMITTABLE)일 때 false를 반환해야 한다", () => {
        expectJudgmentAvailability({ status: MISSION_STATUS.UNSUBMITTABLE }, false);
      });
    });
  });

  describe("fetchJudgmentMissionResult 테스트", () => {
    it("채점 결과를 성공적으로 가져와야 한다", async () => {
      const SAMPLE_PASS_COUNT = 8;
      const SAMPLE_TOTAL_COUNT = 10;

      const mockResponse = {
        data: {
          ...createMissionItem().judgment,
          status: JUDGMENT_STATUS.SUCCEEDED,
          passCount: SAMPLE_PASS_COUNT,
          totalCount: SAMPLE_TOTAL_COUNT,
        },
      };
      (postMyMissionJudgment as jest.Mock).mockResolvedValue(mockResponse);

      const { result } = renderUseMissionJudgment();

      await act(async () => {
        const judgmentResult = await result.current.fetchJudgmentMissionResult();
        expect(judgmentResult).toEqual(
          expect.objectContaining({
            judgment: expect.objectContaining({
              status: JUDGMENT_STATUS.SUCCEEDED,
              passCount: SAMPLE_PASS_COUNT,
              totalCount: SAMPLE_TOTAL_COUNT,
            }),
          })
        );
      });

      expect(postMyMissionJudgment).toHaveBeenCalledWith({
        missionId: createMockMission().id,
        recruitmentId: mockRecruitmentId,
        token: "mockToken",
      });
    });

    it("채점 결과 가져오기 실패 시 에러를 던져야 한다", async () => {
      const errorMessage = "채점 결과를 가져오는 데 실패했습니다.";
      const mockError = { response: { data: { message: errorMessage } } };
      (postMyMissionJudgment as jest.Mock).mockRejectedValue(mockError);

      const { result } = renderUseMissionJudgment();

      await expect(result.current.fetchJudgmentMissionResult()).rejects.toThrow(errorMessage);
    });
  });
});
