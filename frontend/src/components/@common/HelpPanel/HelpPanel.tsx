import { BiHelpCircle } from "react-icons/bi";
import styles from "./HelpPanel.module.css";

const HelpPanel = () => {
  return (
    <div className={styles["help-panel-container"]}>
      <BiHelpCircle className={styles["help-panel"]} />
      <div className={styles["help-modal-container"]}>
        <ul className={styles["help-modal"]}>
          <li className={styles["help-text"]}>예제 테스트 성적은 실제 성적과 무관합니다.</li>
          <li className={styles["help-text"]}>제출완료 후에는 예제 테스트를 실행할 수 없습니다.</li>
          <li className={styles["help-text"]}>
            예제 테스트를 실행하지 않아도 제출 된 Pull Request를 기반으로 채점을 진행합니다.
          </li>
        </ul>
      </div>
    </div>
  );
};

export default HelpPanel;
