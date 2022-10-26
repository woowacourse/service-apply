import classNames from "classnames";
import { useMemo } from "react";
import { generatePath } from "react-router";
import { useNavigate } from "react-router-dom";
import { Recruitment, RecruitmentStatus } from "../../../../types/domains/recruitments";
import { PARAM, PATH } from "../../../constants/path";
import { BUTTON_LABEL } from "../../../pages/MyApplication/MyApplication";
import { formatDateTime } from "../../../utils/format/date";
import { generateQuery } from "../../../utils/route/query";
import ApplyButton from "../MyApplicationButtons/ApplyButton";
import styles from "../MyApplicationItem.module.css";
import RecruitmentDetail from "../RecruitmentDetail/RecruitmentDetail";
import { RECRUITMENT_STATUS } from "./../../../constants/recruitment";

type MyApplicationFormItemProps = {
  recruitment: Recruitment;
  submitted: boolean;
};

const applicationLabel = (submitted: boolean, status: RecruitmentStatus) => {
  if (submitted) {
    return BUTTON_LABEL.COMPLETE;
  }
  switch (status) {
    case RECRUITMENT_STATUS.RECRUITABLE:
      return BUTTON_LABEL.BEFORE_SUBMISSION;
    case RECRUITMENT_STATUS.RECRUITING:
      return BUTTON_LABEL.EDIT;
    case RECRUITMENT_STATUS.UNRECRUITABLE:
      return BUTTON_LABEL.UNSUBMITTABLE;
    case RECRUITMENT_STATUS.ENDED:
      return BUTTON_LABEL.UNSUBMITTED;
    default:
      return BUTTON_LABEL.UNSUBMITTABLE;
  }
};

const isApplicationDisabled = (submitted: boolean, status: RecruitmentStatus) => {
  return submitted || status !== RECRUITMENT_STATUS.RECRUITING;
};

const MyApplicationFormItem = ({ recruitment, submitted }: MyApplicationFormItemProps) => {
  const navigate = useNavigate();

  const formattedStartDateTime = useMemo(
    () => (recruitment.startDateTime ? formatDateTime(new Date(recruitment.startDateTime)) : ""),
    [recruitment.startDateTime]
  );

  const formattedEndDateTime = useMemo(
    () => (recruitment.endDateTime ? formatDateTime(new Date(recruitment.endDateTime)) : ""),
    [recruitment.endDateTime]
  );

  const isButtonDisabled = isApplicationDisabled(submitted, recruitment.status);
  const buttonLabel = applicationLabel(submitted, recruitment.status);

  const routeToApplicationForm = (recruitment: Recruitment) => {
    navigate(
      {
        pathname: generatePath(PATH.APPLICATION_FORM, {
          status: PARAM.APPLICATION_FORM_STATUS.EDIT,
        }),
        search: generateQuery({ recruitmentId: String(recruitment.id) }),
      },
      {
        state: {
          currentRecruitment: recruitment,
        },
      }
    );
  };

  return (
    <div className={classNames(styles["content-box"])}>
      <div className={styles["text-container"]}>
        <RecruitmentDetail startDate={formattedStartDateTime} endDate={formattedEndDateTime}>
          {recruitment.title}
        </RecruitmentDetail>

        <div className={styles["button-container"]}>
          <ApplyButton
            isButtonDisabled={isButtonDisabled}
            onClick={() => {
              routeToApplicationForm(recruitment);
            }}
          >
            {buttonLabel}
          </ApplyButton>
        </div>
      </div>
    </div>
  );
};

export default MyApplicationFormItem;
