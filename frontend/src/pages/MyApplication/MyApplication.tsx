import { useEffect, useMemo, useState } from "react";
import { generatePath } from "react-router";
import { useNavigate } from "react-router-dom";
import { fetchMyApplicationForms } from "../../api/application-forms";
import Container from "../../components/@common/Container/Container";
import Panel from "../../components/@common/Panel/Panel";
import MyApplicationItem from "../../components/MyApplicationItem/MyApplicationItem";
import { ERROR_MESSAGE } from "../../constants/messages";
import PATH, { PARAM } from "../../constants/path";
import useRecruitmentContext from "../../hooks/useRecruitmentContext";
import useTokenContext from "../../hooks/useTokenContext";
import { missionsDummy, myApplicationDummy } from "../../mock/dummy";
import { generateQuery } from "../../utils/route/query";
import { RECRUITMENT_STATUS } from "./../../constants/recruitment";
import styles from "./MyApplication.module.css";

export const BUTTON_LABEL = {
  BEFORE_SUBMISSION: "시작 전",
  EDIT: "수정하기",
  SUBMIT: "제출하기", //작성하기
  UNSUBMITTABLE: "제출불가",
  COMPLETE: "제출완료",
  UNSUBMITTED: "미제출",
} as const;

const missionLabel = (
  submitted: boolean,
  missionStatus: "SUBMITTABLE" | "SUBMITTING" | "UNSUBMITTABLE" | "ENDED"
) => {
  const labelMap = {
    SUBMITTABLE: BUTTON_LABEL.BEFORE_SUBMISSION,
    SUBMITTING: submitted ? BUTTON_LABEL.EDIT : BUTTON_LABEL.SUBMIT,
    UNSUBMITTABLE: BUTTON_LABEL.UNSUBMITTABLE,
    ENDED: submitted ? BUTTON_LABEL.COMPLETE : BUTTON_LABEL.UNSUBMITTED,
  } as const;

  return labelMap[missionStatus];
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

//모집에 관한 타입 ex우아한테크코스 3기
export type Recruitment = {
  id: string;
  title: string;
  recruitable: boolean;
  hidden: boolean;
  startDateTime: `${string}-${string}-${string}T${string}:${string}:${string}`;
  endDateTime: `${string}-${string}-${string}T${string}:${string}:${string}`;
  status: keyof typeof RECRUITMENT_STATUS;
};

// 내 지원서에 대한 목록
export type MyRecruitmentForm = {
  recruitmentId: number;
  submitted: boolean;
};

// 내 지원에 대한 미션의 목록 ex프리코스 3차
export type Mission = {
  id: string;
  title: string;
  description: string;
  startDateTime: `${string}-${string}-${string}T${string}:${string}:${string}`;
  endDateTime: `${string}-${string}-${string}T${string}:${string}:${string}`;
  submitted: boolean;
  submittable: boolean;
  status: "SUBMITTABLE" | "SUBMITTING" | "UNSUBMITTABLE" | "ENDED";
  //newly api
  isAutomation: boolean;
  judgement: {
    testStatus: "NONE" | "PENDING" | "SUCCESS" | "FAIL";
    pullRequestUrl: string;
    commitUrl: string;
    passCount: number;
    totalCount: number;
    message: string; //Pending일때만 빈문자열
  };
};

const MyApplication = () => {
  const navigate = useNavigate();
  const { token } = useTokenContext();
  const { recruitment } = useRecruitmentContext();

  type MyApplication = {
    recruitmentId: number;
    submitted: boolean;
  };
  const [myApplications, setMyApplications] = useState<MyApplication[]>([]);
  const [missions, setMissions] = useState(missionsDummy as unknown as Record<string, Mission[]>);

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

  const recruitmentIds = useMemo(
    () => myApplications.map(({ recruitmentId }) => recruitmentId),
    [myApplications]
  );

  //const { missions } = useMissions(recruitmentIds);

  const routeToApplicationForm = (recruitment: Recruitment) => () => {
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
    ({
      recruitmentId,
      mission,
      submitted,
    }: {
      recruitmentId: string | number;
      mission: Mission;
      submitted: boolean;
    }) =>
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

  const handleFetchError = (error: Error | null) => {
    if (!error) return;

    alert(ERROR_MESSAGE.API.FETCHING_MY_APPLICATIONS);
    setMyApplications([]);
  };

  const handleSetMissions = (missions: Record<string, Mission[]>) => {
    setMissions({ ...missions });
  };

  useEffect(() => {
    try {
      const fetchMyRecruitments = async () => {
        const response = await fetchMyApplicationForms(token);

        //setMyApplications(response.data);
        setMyApplications(myApplicationDummy); //변경
      };

      fetchMyRecruitments();
    } catch (error) {
      if (error instanceof Error) {
        handleFetchError(error);
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
              <MyApplicationItem
                recruitment={{ ...recruitment, title: "내 지원서" }}
                buttonLabel={applicationLabel(submitted, recruitment.recruitable)}
                isButtonDisabled={isApplicationDisabled(submitted, recruitment.recruitable)}
                onClickButton={routeToApplicationForm(recruitment)}
                isMission={false}
                handleSetMissions={handleSetMissions}
              />

              {missions?.[recruitment.id as keyof typeof missions] &&
                missions[recruitment.id as keyof typeof missions].length > 0 && (
                  <hr className={styles.hr} />
                )}

              <div className={styles["application-category"]}>프리코스</div>
              {missions?.[recruitment.id as keyof typeof missions] &&
                missions[recruitment.id as keyof typeof missions].map((mission) => (
                  <MyApplicationItem
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
                    recruitmentId={recruitment.id}
                    missionId={mission.id}
                    isMission={true}
                    handleSetMissions={handleSetMissions}
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
