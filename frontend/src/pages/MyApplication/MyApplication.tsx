import { useEffect, useMemo, useState } from "react";
import { Mission, MyApplicationType, Recruitment } from "../../../types/domains/recruitments";
import { fetchMyApplicationForms } from "../../api/applicationForms";
import Container from "../../components/@common/Container/Container";
import Panel from "../../components/@common/Panel/Panel";
import MyApplicationFormItem from "../../components/MyApplicationItem/MyApplicationFormItem/MyApplicationFormItem";
import MyMissionItem from "../../components/MyApplicationItem/MyMissionItem/MyMissionItem";
import { ERROR_MESSAGE } from "../../constants/messages";
import useRecruitmentContext from "../../hooks/useRecruitmentContext";
import useTokenContext from "../../hooks/useTokenContext";
import useMissions from "./../../hooks/useMissions";
import styles from "./MyApplication.module.css";

const MyApplication = () => {
  const { token } = useTokenContext();
  const { recruitment } = useRecruitmentContext();

  const [myApplications, setMyApplications] = useState<MyApplicationType[]>([]);

  const recruitmentIds = useMemo(
    () => myApplications.map(({ recruitmentId }) => recruitmentId),
    [myApplications]
  );

  const myRecruitments = useMemo(
    () =>
      myApplications
        .map(({ recruitmentId, submitted }) => ({
          ...recruitment.findById(recruitmentId),
          submitted,
        }))
        .reverse() as (Recruitment & { submitted: boolean })[],
    [myApplications, recruitment]
  );

  const { missions } = useMissions(recruitmentIds) as unknown as {
    missions: Record<string, Mission[]>;
  };

  const handleFetchError = () => {
    alert(ERROR_MESSAGE.API.FETCHING_MY_APPLICATIONS);
    setMyApplications([]);
  };

  useEffect(() => {
    try {
      const fetchMyRecruitments = async () => {
        const response = await fetchMyApplicationForms(token);
        const myApplication = response.data as unknown as MyApplicationType[];
        setMyApplications(myApplication);
      };

      fetchMyRecruitments();
    } catch (error) {
      if (error instanceof Error) {
        handleFetchError();
      }
    }
  }, [token]);

  return (
    <div className={styles.box}>
      {/* @ts-ignore */}
      <Container title="내 지원 현황" className={styles["page-title"]} />
      {myRecruitments.map(({ submitted, ...recruitment }, index) => {
        return (
          <Panel
            key={`${index}-${recruitment.id}`}
            title={recruitment.title}
            isOpen={index === 0}
            className={styles["recruit-panel"]}
          >
            <div className={styles["recruit-panel-inner"]}>
              <div className={styles["application-category"]}>지원서</div>
              <MyApplicationFormItem
                recruitment={{ ...recruitment, title: "내 지원서" }}
                submitted={submitted}
              />

              {missions?.[recruitment.id]?.length > 0 && (
                <>
                  <hr className={styles["recruitment-mission-contour"]} />
                  <div className={styles["application-category"]}>프리코스</div>
                </>
              )}

              {missions?.[recruitment.id]?.map((mission) => (
                <MyMissionItem
                  key={`mission-${recruitment.id}-${mission.id}`}
                  mission={mission}
                  recruitmentId={String(recruitment.id)}
                />
              ))}
            </div>
          </Panel>
        );
      })}
    </div>
  );
};

export default MyApplication;
