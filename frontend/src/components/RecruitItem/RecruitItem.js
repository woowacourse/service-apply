import React from "react";
import { useHistory } from "react-router-dom";
import PropTypes from "prop-types";

import CommonItem from "../CommonItem/CommonItem";

import { RECRUITMENT_STATUS } from "../../constants/recruitment";
import PATH from "../../constants/path";

const RecruitItem = ({ recruitment }) => {
  const history = useHistory();

  const isRecruiting = recruitment.status === RECRUITMENT_STATUS.RECRUITING;

  const buttonLabels = {
    [RECRUITMENT_STATUS.RECRUITING]: "지원하기",
    [RECRUITMENT_STATUS.RECRUITABLE]: "모집 예정",
    [RECRUITMENT_STATUS.UNRECRUITABLE]: "일시 중지",
    [RECRUITMENT_STATUS.ENDED]: "모집 종료",
  };
  const buttonLabel = buttonLabels[recruitment.status] || "";

  const goApplicantsNewPage = () => {
    history.push({
      pathname: PATH.NEW_APPLICATION,
      state: {
        recruitmentId: recruitment.id,
      },
    });
  };

  return (
    <CommonItem
      recruitment={recruitment}
      activeButton={isRecruiting}
      buttonLabel={buttonLabel}
      goPage={goApplicantsNewPage}
    />
  );
};

export default RecruitItem;

RecruitItem.propTypes = {
  recruitment: PropTypes.object.isRequired,
};
