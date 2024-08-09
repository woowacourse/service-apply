import { useNavigate, useParams } from "react-router-dom";
import Button, { BUTTON_VARIANT } from "../../components/@common/Button/Button";
import Container from "../../components/@common/Container/Container";
import useTokenContext from "../../hooks/useTokenContext";
import styles from "./AssignmentViewer.module.css";
import { PATH } from "../../constants/path";
import { fetchMissionRequirements } from "../../api";
import { useEffect, useState } from "react";
import highlighter from "highlight.js";
import "github-markdown-css/github-markdown-light.css";
import "highlight.js/styles/github.css";

const AssignmentViewer = () => {
  const navigate = useNavigate();
  const { recruitmentId, missionId } = useParams<{ recruitmentId: string; missionId: string }>();
  const [description, setDescription] = useState<string>("");

  if (!recruitmentId || !missionId) {
    throw new Error("recruitmentId 또는 missionId가 없습니다.");
  }

  const { token } = useTokenContext();

  const goBack = () => {
    navigate(-1);
  };

  const goAssignmentSubmit = () => {
    navigate(PATH.ASSIGNMENT); // TODO: 구현 필요
  };

  const fetchRequirement = async () => {
    try {
      const response = await fetchMissionRequirements({
        token,
        recruitmentId,
        missionId: parseInt(missionId, 10),
      });

      setDescription(response?.data?.description);

      console.log(response);
    } catch (error) {
      console.error(error);
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
          <Button type="button" onClick={goAssignmentSubmit}>
            제출하기
          </Button>
        </li>
      </ul>
    </Container>
  );
};

export default AssignmentViewer;
