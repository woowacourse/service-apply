import { generatePath, useNavigate, useParams } from "react-router-dom";
import Button, { BUTTON_VARIANT } from "../../components/@common/Button/Button";
import Container from "../../components/@common/Container/Container";
import useTokenContext from "../../hooks/useTokenContext";
import styles from "./MissionView.module.css";
import { PARAM, PATH } from "../../constants/path";
import { fetchMissionRequirements } from "../../api";
import { useEffect, useState } from "react";
import highlighter from "highlight.js";
import "github-markdown-css/github-markdown-light.css";
import "highlight.js/styles/github.css";
import { Mission } from "../../../types/domains/recruitments";
import { getMissionLabel } from "../../hooks/useMission";
import { MISSION_STATUS } from "../../constants/recruitment";
import { AxiosError } from "axios";

const MissionView = () => {
  const navigate = useNavigate();

  const { recruitmentId, missionId } = useParams<{ recruitmentId: string; missionId: string }>();
  const [mission, setMission] = useState<Mission | null>(null);
  const description = mission?.description ?? "";

  const submittable = !mission || mission.submittable;
  const missionLabel = getMissionLabel(
    mission?.submitted ?? false,
    mission?.status ?? MISSION_STATUS.UNSUBMITTABLE
  );

  if (!recruitmentId || !missionId) {
    throw new Error("recruitmentId 또는 missionId가 없습니다.");
  }

  const { token } = useTokenContext();

  const goBack = () => {
    navigate(-1);
  };

  const routeToAssignmentSubmit = () => {
    const isSubmitted = mission?.submitted;

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

  const fetchRequirement = async () => {
    try {
      const response = await fetchMissionRequirements({
        token,
        recruitmentId,
        missionId: parseInt(missionId, 10),
      });

      setMission(response?.data);

      console.log(response);
    } catch (error) {
      alert((error as AxiosError).response?.data.message);

      goBack();
    }
  };

  useEffect(() => {
    fetchRequirement();
  }, []);

  useEffect(() => {
    highlighter.highlightAll();
  }, [description]);

  return (
    <Container>
      <div
        className={`${styles["mission-viewer-body"]} markdown-body`}
        dangerouslySetInnerHTML={{ __html: description }}
      />
      <ul className={styles.buttons}>
        <li>
          <Button type="button" variant={BUTTON_VARIANT.OUTLINED} onClick={goBack}>
            뒤로가기
          </Button>
        </li>
        <li>
          <Button type="button" onClick={routeToAssignmentSubmit} disabled={!submittable}>
            {missionLabel}
          </Button>
        </li>
      </ul>
    </Container>
  );
};

export default MissionView;
