import { useNavigate, useParams } from "react-router-dom";
import Button, { BUTTON_VARIANT } from "../../components/@common/Button/Button";
import Container from "../../components/@common/Container/Container";
import useTokenContext from "../../hooks/useTokenContext";
import styles from "./MissionView.module.css";
import { PATH } from "../../constants/path";
import { fetchMissionRequirements } from "../../api";
import { useEffect, useState } from "react";
import highlighter from "highlight.js";
import "github-markdown-css/github-markdown-light.css";
import "highlight.js/styles/github.css";
import { Mission } from "../../../types/domains/recruitments";

const MissionView = () => {
  const navigate = useNavigate();

  const { recruitmentId, missionId } = useParams<{ recruitmentId: string; missionId: string }>();
  const [mission, setMission] = useState<Mission | null>(null);
  const description = mission?.description ?? "";

  if (!recruitmentId || !missionId) {
    throw new Error("recruitmentId 또는 missionId가 없습니다.");
  }

  const { token } = useTokenContext();

  const goBack = () => {
    navigate(-1);
  };

  const goAssignmentSubmit = () => {
    navigate(PATH.ASSIGNMENT, {
      state: {
        recruitmentId,
        currentMission: mission,
      },
    }); // TODO: 구현 필요
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
          {/* TODO:
                제출 완료되었을 경우 -> 제출 완료 && 비활성화
                  -> 미션 기한은 지나지 않았지만, 소감문은 PR 링크와 함께 제출하면, 이후 미션 기한 내 PR 추가 커밋은 반영되나? 또는 제출 내용 수정이 가능한가?
                  -> 미션 기한이 지났는데 제출하지 않은 경우 미제출로 비활성화된 버튼 표시
                제출 기간이 지나면 -> 과제 보기 버튼을 눌러서 진입할 수 있던가?
                  -> Github 저장소 기반 프리코스 진행 시에도 미션 내용은 볼 수 있었으니 페이지에 진입할 수 있도록 하는 것이 맞는지?
          */}
          <Button type="button" onClick={goAssignmentSubmit}>
            제출하기
          </Button>
        </li>
      </ul>
    </Container>
  );
};

export default MissionView;
