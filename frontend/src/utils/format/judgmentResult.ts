import { Mission } from "../../../types/domains/recruitments";
import { JUDGMENT_STATUS } from "../../constants/judgment";
import { isJudgmentTimedOut } from "../validation/judgmentTime";

type JudgmentResultType = { text: string; type: "default" | "fail" | "pass" | "started" | "error" };

const formatJudgmentResult = (judgment: Mission["judgment"]): JudgmentResultType => {
  if (judgment === null) {
    return { text: "없음", type: "default" };
  }

  if (judgment.status === JUDGMENT_STATUS.STARTED && isJudgmentTimedOut(judgment)) {
    return { text: "예기치 못한 오류로 실행 시간을 초과하였습니다.", type: "error" };
  }

  const { passCount, totalCount, status } = judgment;

  switch (status) {
    case JUDGMENT_STATUS.STARTED:
      return { text: "테스트 중", type: "started" };
    case JUDGMENT_STATUS.SUCCEEDED:
      const isPassAllCases = passCount === totalCount;
      return { text: `${passCount} / ${totalCount}`, type: isPassAllCases ? "pass" : "fail" };
    case JUDGMENT_STATUS.FAILED:
    case JUDGMENT_STATUS.CANCELLED:
      return { text: "예기치 못한 오류로 인하여 실행에 실패하였습니다.", type: "error" };
    default:
      return { text: "", type: "default" };
  }
};

export default formatJudgmentResult;
