import React from "react";
import { useHistory } from "react-router-dom";

const RecruitItem = ({
  recruitment,
  buttonLabel,
  isRecruiting,
  goApplicantsNewPage,
}) => {
  const history = useHistory();

  const isRecruiting = recruitment.status === "RECRUITING";

  const buttonLabels = {
    RECRUITING: "지원하기",
    RECRUITABLE: "모집 예정",
    UNRECRUITABLE: "일시 중지",
    ENDED: "모집 종료",
  };
  const buttonLabel = buttonLabels[recruitment.status] || "";

  const goApplicantsNewPage = () => {
    history.push({
      pathname: `/applicants/new`,
      state: {
        recruitmentId,
      },
    });
  };

  return (
    <CommonItem
      recruitment={recruitment}
      isRecruiting={isRecruiting}
      buttonLabel={buttonLabel}
      goPage={goApplicantsNewPage}
    />
  );
};

export default RecruitItem;
