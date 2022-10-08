import classNames from "classnames";
import { Mission, Recruitment } from "../../../../../types/domains/recruitments";
import { fetchMyMissionJudgment } from "../../../../api";
import { MISSION_STATUS } from "../../../../constants/recruitment";
import useTokenContext from "../../../../hooks/useTokenContext";
import Button from "../../../@common/Button/Button";
import { postMyMissionJudgment } from "./../../../../api/recruitments";
import { JUDGMENT_STATUS } from "./../../../../constants/judgment";
import { BUTTON_VARIANT } from "./../../../@common/Button/Button";
import styles from "./ApplicationButtons.module.css";

type ContainerProps = {
  children: React.ReactNode;
};

type RefreshProps = {
  recruitmentId?: Recruitment["id"];
  missionItem: Mission;
  setMission: React.Dispatch<React.SetStateAction<Mission>>;
};

type JudgmentProps = {
  recruitmentId: Recruitment["id"];
  missionItem: Mission;
  setMission: React.Dispatch<React.SetStateAction<Mission>>;
};

type ApplyProps = {
  children: string;
  isButtonDisabled: boolean;
  onClick: () => void;
};

const Container = ({ children }: ContainerProps) => {
  return <div className={styles["button-container"]}>{children}</div>;
};

const Refresh = ({ recruitmentId, missionItem, setMission }: RefreshProps) => {
  const { token } = useTokenContext();
  const missionId = missionItem.id;
  const status = missionItem.judgment?.status;

  if (status !== JUDGMENT_STATUS.STARTED) {
    return null;
  }

  if (missionId === undefined || recruitmentId === undefined) {
    return null;
  }

  const handleRefreshMission = async ({
    missionId,
    recruitmentId,
    token,
  }: {
    missionId: string;
    recruitmentId: string;
    token: string;
  }) => {
    const response = await fetchMyMissionJudgment({
      recruitmentId: Number(recruitmentId),
      missionId: Number(missionId),
      token,
    });

    setMission({ ...missionItem, judgment: response.data });
  };

  return (
    <Button
      className={classNames(styles["refresh-button"])}
      type="button"
      variant={BUTTON_VARIANT.CONTAINED}
      cancel={false}
      onClick={() => {
        handleRefreshMission({
          missionId: String(missionId),
          recruitmentId: String(recruitmentId),
          token,
        });
      }}
    >
      새로고침
    </Button>
  );
};

const Judgment = ({ missionItem, recruitmentId, setMission }: JudgmentProps) => {
  const { token } = useTokenContext();
  const missionStatus = missionItem.status;
  const judgment = missionItem.judgment;

  if (judgment === null) {
    return null;
  }

  const handleJudgeMission = async ({
    missionId,
    recruitmentId,
    token,
  }: {
    missionId: string;
    recruitmentId: string;
    token: string;
  }) => {
    const response = await postMyMissionJudgment({
      recruitmentId: Number(recruitmentId),
      missionId: Number(missionId),
      token,
    });
    setMission({ ...missionItem, judgment: response.data });
  };

  return (
    <Button
      className={classNames(styles["judgment-button"])}
      type="button"
      variant={BUTTON_VARIANT.CONTAINED}
      cancel={false}
      disabled={
        judgment.status === JUDGMENT_STATUS.STARTED ||
        missionStatus === MISSION_STATUS.ENDED ||
        missionStatus === MISSION_STATUS.UNSUBMITTABLE
      }
      onClick={() => {
        handleJudgeMission({
          missionId: String(missionItem.id),
          recruitmentId: String(recruitmentId),
          token,
        });
      }}
    >
      예제 테스트 실행
    </Button>
  );
};

const Apply = ({ children, isButtonDisabled, onClick }: ApplyProps) => {
  return (
    <Button
      className={classNames(styles["apply-button"])}
      type="button"
      variant={BUTTON_VARIANT.CONTAINED}
      cancel={false}
      disabled={isButtonDisabled}
      onClick={onClick}
    >
      {children}
    </Button>
  );
};

const ApplicationButtons = Object.assign(Container, {
  Judgment,
  Apply,
  Refresh,
});

export default ApplicationButtons;
