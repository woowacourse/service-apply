import classNames from "classnames";
import { Mission } from "../../../../types/domains/recruitments";
import { MISSION_STATUS } from "../../../constants/recruitment";
import MissionDetail from "./MissionDetail/MissionDetail";
import styles from "../MyApplicationItem.module.css";
import buttonStyles from "./ApplicationButtonStyles.module.css";
import RecruitmentDetail from "../RecruitmentDetail/RecruitmentDetail";
import useMission from "../../../hooks/useMission";
import Button from "../../@common/Button/Button";
import { generatePath, useNavigate } from "react-router-dom";
import { PARAM, PATH } from "../../../constants/path";

type MyMissionItemProps = {
  mission: Mission;
  recruitmentId: string;
};

const MyMissionItem = ({ mission, recruitmentId }: MyMissionItemProps) => {
  const navigate = useNavigate();

  const { missionItem, applyButtonLabel, formattedStartDateTime, formattedEndDateTime } =
    useMission({ mission, recruitmentId });

  const routeToAssignmentSubmit =
    ({ recruitmentId, mission }: { recruitmentId: string; mission: Mission }) =>
    () => {
      const isSubmitted = mission.submitted;

      navigate(
        {
          pathname: generatePath(PATH.ASSIGNMENT, {
            status: isSubmitted ? PARAM.ASSIGNMENT_STATUS.EDIT : PARAM.ASSIGNMENT_STATUS.NEW,
          }),
        },
        {
          state: {
            recruitmentId,
            currentMission: mission,
          },
        }
      );
    };

  return (
    <div className={classNames(styles["content-box"])}>
      <div className={styles["content-wrapper"]}>
        <div className={styles["title-container"]}>
          <RecruitmentDetail startDate={formattedStartDateTime} endDate={formattedEndDateTime}>
            {missionItem.title}
          </RecruitmentDetail>
          <ul className={styles["title-button-list"]}>
            <li>
              <Button
                className={buttonStyles["assignment-button"]}
                onClick={() => {
                  navigate(
                    generatePath(PATH.ASSIGNMENT_VIEW, {
                      recruitmentId,
                      missionId: String(mission.id),
                    })
                  );
                }}
              >
                과제 보기
              </Button>
            </li>
            <li>
              <Button
                className={buttonStyles["apply-button"]}
                disabled={missionItem.status !== MISSION_STATUS.SUBMITTING}
                onClick={routeToAssignmentSubmit({
                  recruitmentId: String(recruitmentId),
                  mission,
                })}
              >
                {applyButtonLabel}
              </Button>
            </li>
          </ul>
        </div>
        <hr className={styles["divider"]} />
        <MissionDetail
          mission={mission}
          recruitmentId={recruitmentId}
          judgment={missionItem.judgment}
        />
      </div>
    </div>
  );
};

export default MyMissionItem;
