import React from "react";
import { useHistory } from "react-router-dom";
import { CommonItem } from "../CommonItem/CommonItem";

const ApplicationFormItem = ({ recruitment, submitted }) => {
  const history = useHistory();

  const submittable = !submitted && recruitment.status === "RECRUITING";

  const buttonLabel =
    (submitted && "제출 완료") || (submittable ? "지원서 수정" : "기간 만료");

  const goApplicationFormsEditPage = () => {
    history.push({
      pathname: `/application-forms/edit`,
      state: {
        recruitmentId: recruitment.id,
      },
    });
  };

  return (
    <CommonItem
      recruitment={recruitment}
      buttonLabel={buttonLabel}
      submittable={submittable}
      goPage={goApplicationFormsEditPage}
    />
  );
};

export default ApplicationFormItem;
