import { useEffect, useMemo, useState } from "react";
import { generatePath } from "react-router";
import { useNavigate } from "react-router-dom";
import { fetchMyApplicationForms } from "../../api/application-forms";
import Container, { TITLE_ALIGN } from "../../components/@common/Container/Container";
import Panel from "../../components/@common/Panel/Panel";
import RecruitmentItem from "../../components/RecruitmentItem/RecruitmentItem";
import { ERROR_MESSAGE } from "../../constants/messages";
import { PATH, PARAM } from "../../constants/path";
import useMissions from "../../hooks/useMissions";
import useRecruitmentContext from "../../hooks/useRecruitmentContext";
import useTokenContext from "../../hooks/useTokenContext";
import { generateQuery } from "../../utils/route/query";
import styles from "./MyApplication.module.css";

const BUTTON_LABEL = {
  BEFORE_SUBMISSION: "시작 전",
  EDIT: "수정하기",
  SUBMIT: "제출하기",
  UNSUBMITTABLE: "제출불가",
  COMPLETE: "제출완료",
  UNSUBMITTED: "미제출",
};

const missionLabel = (submitted, missionStatus) => {
  const labelMap = {
    SUBMITTABLE: BUTTON_LABEL.BEFORE_SUBMISSION,
    SUBMITTING: submitted ? BUTTON_LABEL.EDIT : BUTTON_LABEL.SUBMIT,
    UNSUBMITTABLE: BUTTON_LABEL.UNSUBMITTABLE,
    ENDED: submitted ? BUTTON_LABEL.COMPLETE : BUTTON_LABEL.UNSUBMITTED,
  };

  return labelMap[missionStatus];
};

const applicationLabel = (submitted, recruitable) => {
  if (!recruitable) {
    return BUTTON_LABEL.UNSUBMITTABLE;
  }

  return submitted ? BUTTON_LABEL.COMPLETE : BUTTON_LABEL.EDIT;
};

const isApplicationDisabled = (submitted, recruitable) => {
  return submitted || !recruitable;
};

const MyApplication = () => {
  const navigate = useNavigate();
  const { token } = useTokenContext();
  const { recruitment } = useRecruitmentContext();

  const [myApplications, setMyApplications] = useState([]);
  const myRecruitments = useMemo(
    () =>
      myApplications
        .map(({ recruitmentId, submitted }) => ({
          ...recruitment.findById(recruitmentId),
          submitted,
        }))
        .reverse(),
    [myApplications, recruitment]
  );

  const recruitmentIds = useMemo(
    () => myApplications.map(({ recruitmentId }) => recruitmentId),
    [myApplications]
  );

  const { missions } = useMissions(recruitmentIds);

  const routeToApplicationForm = (recruitment) => () => {
    navigate(
      {
        pathname: generatePath(PATH.APPLICATION_FORM, {
          status: PARAM.APPLICATION_FORM_STATUS.EDIT,
        }),
        search: generateQuery({ recruitmentId: recruitment.id }),
      },
      {
        state: {
          currentRecruitment: recruitment,
        },
      }
    );
  };

  const routeToAssignmentSubmit =
    ({ recruitmentId, mission, submitted }) =>
    () => {
      if (submitted) {
        navigate(
          {
            pathname: generatePath(PATH.ASSIGNMENT, {
              status: PARAM.ASSIGNMENT_STATUS.EDIT,
            }),
          },
          {
            state: {
              recruitmentId,
              currentMission: mission,
            },
          }
        );
      } else {
        navigate(
          {
            pathname: generatePath(PATH.ASSIGNMENT, {
              status: PARAM.ASSIGNMENT_STATUS.NEW,
            }),
          },
          {
            state: {
              recruitmentId,
              currentMission: mission,
            },
          }
        );
      }
    };

  const handleFetchError = (error) => {
    if (!error) return;

    alert(ERROR_MESSAGE.API.FETCHING_MY_APPLICATIONS);
    setMyApplications([]);
  };

  useEffect(() => {
    try {
      const fetchMyRecruitments = async () => {
        const response = await fetchMyApplicationForms(token);

        setMyApplications(response.data);
      };

      fetchMyRecruitments();
    } catch (error) {
      handleFetchError(error);
    }
  }, [token]);

  return (
    <div className={styles.box}>
      <Container
        title="내 지원 현황"
        titleAlign={TITLE_ALIGN.LEFT}
        className={styles["page-title"]}
      />
      {myRecruitments.map(({ submitted, ...recruitment }, index) => (
        <Panel
          key={`recruitment-${recruitment.id}`}
          title={recruitment.title}
          isOpen={index === 0}
          className={styles["recruit-panel"]}
        >
          <div className={styles["recruit-panel-inner"]}>
            <RecruitmentItem
              recruitment={{ ...recruitment, title: "내 지원서" }}
              buttonLabel={applicationLabel(submitted, recruitment.recruitable)}
              isButtonDisabled={isApplicationDisabled(submitted, recruitment.recruitable)}
              onClickButton={routeToApplicationForm(recruitment)}
            />

            {missions?.[recruitment.id] && missions[recruitment.id].length > 0 && (
              <hr className={styles.hr} />
            )}

            {missions?.[recruitment.id] &&
              missions[recruitment.id].map((mission) => (
                <RecruitmentItem
                  key={`mission-${recruitment.id}-${mission.id}`}
                  className={styles["mission-recruit-item"]}
                  recruitment={mission}
                  buttonLabel={missionLabel(mission.submitted, mission.status)}
                  isButtonDisabled={mission.status !== "SUBMITTING"}
                  onClickButton={routeToAssignmentSubmit({
                    recruitmentId: recruitment.id,
                    mission,
                    submitted: mission.submitted,
                  })}
                />
              ))}
          </div>
        </Panel>
      ))}
    </div>
  );
};

export default MyApplication;
