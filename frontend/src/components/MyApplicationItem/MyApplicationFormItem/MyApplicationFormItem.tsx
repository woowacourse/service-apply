import classNames from "classnames";
import { useMemo } from "react";
import { generatePath } from "react-router";
import { useNavigate } from "react-router-dom";
import { Recruitment, RecruitmentStatus } from "../../../../types/domains/recruitments";
import { PATH } from "../../../constants/path";
import { formatDateTime } from "../../../utils/format/date";
import { generateQuery } from "../../../utils/route/query";
import ApplyButton from "../MyApplicationButtons/ApplyButton";
import styles from "../MyApplicationItem.module.css";
import RecruitmentDetail from "../RecruitmentDetail/RecruitmentDetail";
import { BUTTON_LABEL, RECRUITMENT_STATUS } from "./../../../constants/recruitment";

type MyApplicationFormItemProps = {
  recruitment: Recruitment;
  submitted: boolean;
};

const APPLICATION_STATUS = {
  [RECRUITMENT_STATUS.RECRUITABLE]: BUTTON_LABEL.BEFORE_SUBMIT,
  [RECRUITMENT_STATUS.RECRUITING]: BUTTON_LABEL.EDIT,
  [RECRUITMENT_STATUS.UNRECRUITABLE]: BUTTON_LABEL.UNSUBMITTABLE,
  [RECRUITMENT_STATUS.ENDED]: BUTTON_LABEL.UNSUBMITTED,
} as const;

const applicationLabel = (submitted: boolean, status: RecruitmentStatus) => {
  if (submitted) {
    return BUTTON_LABEL.COMPLETE;
  }
  return APPLICATION_STATUS[status];
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
  const applyButtonLabel = applicationLabel(submitted, recruitment.status);

  const routeToApplicationForm = (recruitment: Recruitment) => {
    navigate(
      {
        pathname: generatePath(PATH.APPLICATION_FORM),
        search: generateQuery({ recruitmentId: String(recruitment.id) }),
      },
      {
        state: {
          currentRecruitment: recruitment, /// TODO: 여기도 수정
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
            label={applyButtonLabel}
          />
        </div>
      </div>
    </div>
  );
};

export default MyApplicationFormItem;
