import { useEffect, useState, useMemo } from "react";
import { generatePath, useHistory } from "react-router";
import { fetchMyApplicationForms } from "../../api/application-forms";
import Container from "../../components/@common/Container/Container";
import Panel from "../../components/@common/Panel/Panel";
import RecruitmentItem from "../../components/RecruitmentItem/RecruitmentItem";
import PATH, { PARAM } from "../../constants/path";
import useRecruitmentContext from "../../hooks/useRecruitmentContext";
import useTokenContext from "../../hooks/useTokenContext";
import { missionDummy } from "../../mock/dummy";
import { generateQuery } from "../../utils/route/query";
import styles from "./MyApplication.module.css";

const isBeforeDeadline = (endDate) => new Date() < new Date(endDate);

const missionLabel = (submitted, isBeforeDeadline) => {
  if (isBeforeDeadline) {
    return submitted ? "수정하기" : "제출하기";
  }

  return submitted ? "제출완료" : "미제출";
};

const MyApplication = () => {
  const history = useHistory();
  const { token } = useTokenContext();
  const { recruitment } = useRecruitmentContext();
  const [myApplications, setMyApplications] = useState([]);

  const myRecruitments = useMemo(
    () =>
      myApplications.map(({ recruitmentId, submitted }) => ({
        ...recruitment.findById(recruitmentId),
        submitted,
      })),
    [myApplications, recruitment]
  );

  const routeToApplicationForm = (recruitment) => () => {
    history.push({
      pathname: generatePath(PATH.APPLICATION_FORM, {
        status: PARAM.APPLICATION_FORM_STATUS.EDIT,
      }),
      search: generateQuery({ recruitmentId: recruitment.id }),
      state: {
        currentRecruitment: recruitment,
      },
    });
  };

  useEffect(() => {
    try {
      const fetchMyRecruitments = async () => {
        const response = await fetchMyApplicationForms(token);

        setMyApplications(response.data);
      };

      fetchMyRecruitments();
    } catch (e) {
      console.error(e);
      setMyApplications([]);
    }
  }, [token]);

  return (
    <div className={styles.box}>
      <Container title="내 지원 현황" className={styles["page-title"]} />
      {myRecruitments.map(
        ({ submitted, ...recruitment }, index) =>
          !recruitment.hidden && (
            <Panel
              key={`recruitment-${recruitment.id}`}
              title={recruitment.title}
              initialOpen={index === 0}
              className={styles["recruit-panel"]}
            >
              <div className={styles["recruit-panel-inner"]}>
                <RecruitmentItem
                  recruitment={{ ...recruitment, title: "내 지원서" }}
                  buttonLabel={submitted ? "제출완료" : "수정하기"}
                  isButtonDisabled={!submitted}
                  onClickButton={routeToApplicationForm(recruitment)}
                />

                <hr className={styles.hr} />

                {missionDummy.map(({ submitted, ...mission }, index) => (
                  <RecruitmentItem
                    key={`mission-${recruitment.id}-${index}`}
                    className={styles["mission-recruit-item"]}
                    recruitment={mission}
                    buttonLabel={missionLabel(
                      submitted,
                      isBeforeDeadline(mission.endDateTime)
                    )}
                    isButtonDisabled={isBeforeDeadline(mission.endDateTime)}
                  />
                ))}
              </div>
            </Panel>
          )
      )}
    </div>
  );
};

export default MyApplication;
