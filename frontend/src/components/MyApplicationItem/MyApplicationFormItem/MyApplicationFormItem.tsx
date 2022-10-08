import classNames from "classnames";
import { useMemo } from "react";
import { generatePath } from "react-router";
import { useNavigate } from "react-router-dom";
import { Recruitment } from "../../../../types/domains/recruitments";
import { PARAM, PATH } from "../../../constants/path";
import { BUTTON_LABEL } from "../../../pages/MyApplication/MyApplication";
import { formatDateTime } from "../../../utils/format/date";
import { generateQuery } from "../../../utils/route/query";
import ApplyButton from "../MyApplicationButtons/ApplyButton";
import styles from "../MyApplicationItem.module.css";
import RecruitmentDetail from "../RecruitmentDetail/RecruitmentDetail";

type MyApplicationFormItemProps = {
  recruitment: Recruitment;
  submitted: boolean;
};

const applicationLabel = (submitted: boolean, recruitable: boolean) => {
  if (!recruitable) {
    return BUTTON_LABEL.UNSUBMITTABLE;
  }

  return submitted ? BUTTON_LABEL.COMPLETE : BUTTON_LABEL.EDIT;
};

const isApplicationDisabled = (submitted: boolean, recruitable: boolean) => {
  return submitted || !recruitable;
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

  const isButtonDisabled = isApplicationDisabled(submitted, recruitment.recruitable);
  const buttonLabel = applicationLabel(submitted, recruitment.recruitable);

  const routeToApplicationForm = (recruitment: Recruitment) => () => {
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
