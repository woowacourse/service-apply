import { render, screen, fireEvent } from "@testing-library/react";
import { generatePath, MemoryRouter } from "react-router-dom";
import "@testing-library/jest-dom";
import MyMissionItem from "../MyMissionItem";
import useRefresh from "../../../../hooks/useRefresh";
import useMissionJudgment from "../../../../hooks/useMissionJudgment";
import { createMockMission } from "./testMissionMockUtils";
import { PARAM, PATH } from "../../../../constants/path";
import {
  BUTTON_LABEL,
  MISSION_STATUS,
  MISSION_SUBMISSION_METHOD,
} from "../../../../constants/recruitment";

jest.mock("../../../../hooks/useRefresh");
jest.mock("../../../../hooks/useMissionJudgment");

const mockNavigate = jest.fn();
jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useNavigate: () => mockNavigate,
}));

describe("MyMissionItem 컴포넌트 테스트", () => {
  const RECRUITMENT_ID = "123";
  const MISSION_ITEM = createMockMission();

  beforeEach(() => {
    jest.clearAllMocks();
    (useRefresh as jest.Mock).mockReturnValue({ requestRefresh: jest.fn() });
    (useMissionJudgment as jest.Mock).mockReturnValue({
      isJudgmentAvailable: false,
      judgment: null,
      requestMissionJudgment: jest.fn(),
    });
  });

  describe("렌더링 테스트", () => {
    it("컴포넌트가 올바르게 렌더링되어야 한다", () => {
      render(
        <MemoryRouter>
          <MyMissionItem recruitmentId={RECRUITMENT_ID} mission={MISSION_ITEM} />
        </MemoryRouter>
      );

      expect(screen.getByText(MISSION_ITEM.title)).toBeInTheDocument();
    });

    it("미션 상태가 제출 중이 아닐 때 과제 제출 버튼이 비활성화되어야 한다", () => {
      const notSubmittingMission = createMockMission({
        status: MISSION_STATUS.IN_PROGRESS,
        submitted: false,
      });

      render(
        <MemoryRouter>
          <MyMissionItem recruitmentId={RECRUITMENT_ID} mission={notSubmittingMission} />
        </MemoryRouter>
      );

      const submitButton = screen.getByText(BUTTON_LABEL.BEFORE_SUBMIT);
      expect(submitButton).toBeDisabled();
    });

    it("예제 테스트 실행 중이 아닐 때는 새로고침 버튼이 보이지 않아야 한다", () => {
      render(
        <MemoryRouter>
          <MyMissionItem recruitmentId={RECRUITMENT_ID} mission={MISSION_ITEM} />
        </MemoryRouter>
      );

      expect(screen.queryByText(BUTTON_LABEL.REFRESH)).not.toBeInTheDocument();
    });

    it("예제 테스트를 할 수 없는 상태일 때, 예제 테스트 실행 버튼이 비활성화되어야 한다", () => {
      render(
        <MemoryRouter>
          <MyMissionItem recruitmentId={RECRUITMENT_ID} mission={MISSION_ITEM} />
        </MemoryRouter>
      );

      const runExampleTestButton = screen.getByText(BUTTON_LABEL.JUDGMENT);
      expect(runExampleTestButton).toBeDisabled();
    });

    it("일반 URL 제출 방식일 때, 예제 테스트 실행 버튼이 보이지 않아야 한다", () => {
      const GENERIC_URL_MISSION = createMockMission({
        submissionMethod: MISSION_SUBMISSION_METHOD.GENERIC_URL,
        status: MISSION_STATUS.SUBMITTING,
        submitted: true,
        testable: false,
        judgment: null,
      });

      render(
        <MemoryRouter>
          <MyMissionItem recruitmentId={RECRUITMENT_ID} mission={GENERIC_URL_MISSION} />
        </MemoryRouter>
      );
      expect(screen.queryByText(BUTTON_LABEL.JUDGMENT)).not.toBeInTheDocument();
    });
  });

  describe("라우팅 테스트", () => {
    it("미션 과제 제출물 미제출 시, 'new' 상태로 과제 제출 페이지로 이동해야 한다", () => {
      const mockMission = createMockMission({
        submitted: false,
        status: MISSION_STATUS.SUBMITTING,
      });

      render(
        <MemoryRouter>
          <MyMissionItem recruitmentId={RECRUITMENT_ID} mission={mockMission} />
        </MemoryRouter>
      );

      const submitButton = screen.getByText(BUTTON_LABEL.SUBMIT);
      expect(submitButton).toBeEnabled();
      fireEvent.click(submitButton);

      expect(mockNavigate).toHaveBeenCalledWith(
        {
          pathname: generatePath(PATH.ASSIGNMENT, { status: PARAM.ASSIGNMENT_STATUS.NEW }),
        },
        {
          state: {
            recruitmentId: RECRUITMENT_ID,
            currentMission: mockMission,
          },
        }
      );
    });

    it("미션 과제 제출물 제출 시, 'edit' 상태로 과제 제출 페이지로 이동해야 한다", () => {
      const mockMission = createMockMission({ submitted: true, status: MISSION_STATUS.SUBMITTING });

      render(
        <MemoryRouter>
          <MyMissionItem recruitmentId={RECRUITMENT_ID} mission={mockMission} />
        </MemoryRouter>
      );

      const submitButton = screen.getByText(BUTTON_LABEL.EDIT);
      expect(submitButton).toBeEnabled();
      fireEvent.click(submitButton);

      expect(mockNavigate).toHaveBeenCalledWith(
        {
          pathname: generatePath(PATH.ASSIGNMENT, { status: PARAM.ASSIGNMENT_STATUS.EDIT }),
        },
        {
          state: {
            recruitmentId: RECRUITMENT_ID,
            currentMission: mockMission,
          },
        }
      );
    });
  });
});
