import { Mission } from "../../../types/domains/recruitments";
import { JUDGMENT_STATUS } from "../../constants/judgment";
import { isJudgmentTimedOut } from "../validation/judgmentTime";

type JudgmentResultType = { text: string; type: "default" | "fail" | "pass" | "started" };

const formatJudgmentResult = (judgment: Mission["judgment"]): JudgmentResultType => {
  if (judgment === null) {
    return { text: "없음", type: "default" };
  }

  if (isJudgmentTimedOut(judgment.startedDateTime)) {
    return { text: "예기치 못한 오류로 실행 시간을 초과하였습니다.", type: "fail" };
  }

  const { passCount, totalCount, status } = judgment;

  switch (status) {
    case JUDGMENT_STATUS.STARTED:
      return { text: "테스트 중", type: "started" };
    case JUDGMENT_STATUS.SUCCEEDED:
      const isPass = passCount / totalCount === 1;
      return { text: `${passCount} / ${totalCount}`, type: isPass ? "pass" : "fail" };
    case JUDGMENT_STATUS.FAILED:
    case JUDGMENT_STATUS.CANCELLED:
      return { text: "예기치 못한 오류로 인하여 실행에 실패하였습니다.", type: "fail" };
    default:
      return { text: "", type: "default" };
  }
};

export default formatJudgmentResult;
