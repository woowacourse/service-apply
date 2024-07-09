import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import MyMissionItem from "../MyMissionItem";
import * as useMissionModule from "../useMission";
import { createMockMission } from "./testMissionMockUtils";
import { MISSION_STATUS } from "../../../../constants/recruitment";

jest.mock("../useMission");

const LABELS = {
  APPLY: "과제 제출",
  REFRESH: "새로고침",
  JUDGMENT: "예제 테스트 실행",
};

describe("MyMissionItem 통합 테스트", () => {
  const mockRecruitmentId = "123";
  const mockMission = createMockMission();

  const mockUseMission = {
    getter: {
      missionItem: mockMission,
      applyButtonLabel: LABELS.APPLY,
      formattedStartDateTime: "2023-01-01 00:00",
      formattedEndDateTime: "2023-12-31 23:59",
      isJudgmentAvailable: true,
      isRefreshAvailable: true,
    },
    routeToAssignmentSubmit: jest.fn(),
    requestRefresh: jest.fn(),
    requestMissionJudgment: jest.fn(),
  };

  beforeEach(() => {
    (useMissionModule.default as jest.Mock).mockReturnValue(mockUseMission);
  });

  describe("렌더링 테스트", () => {
    it("컴포넌트가 올바르게 렌더링되어야 한다", () => {
      render(
        <MemoryRouter>
          <MyMissionItem mission={mockMission} recruitmentId={mockRecruitmentId} />
        </MemoryRouter>
      );

      expect(screen.getByText(mockMission.title)).toBeInTheDocument();
      expect(screen.getByText(LABELS.APPLY)).toBeInTheDocument();
      expect(screen.getByText(LABELS.REFRESH)).toBeInTheDocument();
      expect(screen.getByText(LABELS.JUDGMENT)).toBeInTheDocument();
    });

    it("미션 상태가 SUBMITTING이 아닐 때 과제 제출 버튼이 비활성화되어야 한다", () => {
      const notSubmittingMission = createMockMission({ status: MISSION_STATUS.ENDED });
      (useMissionModule.default as jest.Mock).mockReturnValue({
        ...mockUseMission,
        getter: { ...mockUseMission.getter, missionItem: notSubmittingMission },
      });

      render(
        <MemoryRouter>
          <MyMissionItem mission={notSubmittingMission} recruitmentId={mockRecruitmentId} />
        </MemoryRouter>
      );

      expect(screen.getByText(LABELS.APPLY)).toBeDisabled();
    });

    it("judgment가 없을 때 새로고침 버튼이 보이지 않아야 한다", () => {
      const missionWithoutJudgment = createMockMission({ judgment: null });
      (useMissionModule.default as jest.Mock).mockReturnValue({
        ...mockUseMission,
        getter: {
          ...mockUseMission.getter,
          missionItem: missionWithoutJudgment,
          isRefreshAvailable: false,
        },
      });

      render(
        <MemoryRouter>
          <MyMissionItem mission={missionWithoutJudgment} recruitmentId={mockRecruitmentId} />
        </MemoryRouter>
      );

      expect(screen.queryByText(LABELS.REFRESH)).not.toBeInTheDocument();
    });

    it("isJudgmentAvailable이 false일 때 예제 테스트 실행 버튼이 비활성화되어야 한다", () => {
      (useMissionModule.default as jest.Mock).mockReturnValue({
        ...mockUseMission,
        getter: { ...mockUseMission.getter, isJudgmentAvailable: false },
      });

      render(
        <MemoryRouter>
          <MyMissionItem mission={mockMission} recruitmentId={mockRecruitmentId} />
        </MemoryRouter>
      );

      expect(screen.getByText(LABELS.JUDGMENT)).toBeDisabled();
    });
  });

  describe("기능 테스트", () => {
    it("과제 제출 버튼 클릭 시 routeToAssignmentSubmit 함수가 호출되어야 한다", () => {
      render(
        <MemoryRouter>
          <MyMissionItem mission={mockMission} recruitmentId={mockRecruitmentId} />
        </MemoryRouter>
      );

      fireEvent.click(screen.getByText(LABELS.APPLY));
      expect(mockUseMission.routeToAssignmentSubmit).toHaveBeenCalled();
    });

    it("새로고침 버튼 클릭 시 requestRefresh 함수가 호출되어야 한다", async () => {
      render(
        <MemoryRouter>
          <MyMissionItem mission={mockMission} recruitmentId={mockRecruitmentId} />
        </MemoryRouter>
      );

      fireEvent.click(screen.getByText(LABELS.REFRESH));
      await waitFor(() => {
        expect(mockUseMission.requestRefresh).toHaveBeenCalled();
      });
    });

    it("예제 테스트 실행 버튼 클릭 시 requestMissionJudgment 함수가 호출되어야 한다", async () => {
      render(
        <MemoryRouter>
          <MyMissionItem mission={mockMission} recruitmentId={mockRecruitmentId} />
        </MemoryRouter>
      );

      fireEvent.click(screen.getByText(LABELS.JUDGMENT));
      await waitFor(() => {
        expect(mockUseMission.requestMissionJudgment).toHaveBeenCalled();
      });
    });
  });
});
