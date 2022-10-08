import classNames from "classnames";
import { useMemo } from "react";
import { generatePath } from "react-router";
import { useNavigate } from "react-router-dom";
import { Recruitment } from "../../../types/domains/recruitments";
import { BUTTON_LABEL } from "../../pages/MyApplication/MyApplication";
import { formatDateTime } from "../../utils/format/date";
import { generateQuery } from "../../utils/route/query";
import { PARAM, PATH } from "./../../constants/path";
import ApplicationButtons from "./compounds/ApplicationButtons/ApplicationButtons";
import RecruitmentDetail from "./compounds/RecruitmentDetail/RecruitmentDetail";
import styles from "./MyApplicationItem.module.css";

type MyRecruitmentItemProps = {
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

const MyRecruitmentItem = ({ recruitment, submitted }: MyRecruitmentItemProps) => {
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
    <div className={classNames(styles["content-wrapper"])}>
      <div className={styles["text-container"]}>
        <RecruitmentDetail>
          <RecruitmentDetail.Title>{recruitment.title}</RecruitmentDetail.Title>
          <RecruitmentDetail.Date
            startDate={formattedStartDateTime}
            endDate={formattedEndDateTime}
          />
        </RecruitmentDetail>
        <ApplicationButtons>
          <ApplicationButtons.Apply
            isButtonDisabled={isButtonDisabled}
            onClick={() => {
              routeToApplicationForm(recruitment);
            }}
          >
            {buttonLabel}
          </ApplicationButtons.Apply>
        </ApplicationButtons>
      </div>
    </div>
  );
};

export default MyRecruitmentItem;
