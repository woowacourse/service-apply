import { ChangeEvent, useState } from "react";
import useModalContext from "../../../hooks/useModalContext";
import Button from "../../@common/Button/Button";
import styles from "./ApplicationPreviewModalWindow.module.css";
import ModalWindow from "../ModalWindow/ModalWindow";
import CheckBox from "../../form/CheckBox/CheckBox";

type RecruitmentItem = {
  recruitmentId: number;
  id: number;
  position: number;
  title: string;
  description: string;
  maximumLength: number;
};

type ApplicationPreviewModalWindowProps = {
  recruitmentItems: RecruitmentItem[];
  answers: string[];
  referenceUrl: string;
  onClickConfirmButton: React.MouseEventHandler<HTMLButtonElement>;
  onClickDismissButton?: React.MouseEventHandler<HTMLButtonElement>;
};

const ApplicationPreviewModalWindow = ({
  recruitmentItems,
  answers,
  referenceUrl,
  onClickConfirmButton,
  onClickDismissButton,
}: ApplicationPreviewModalWindowProps) => {
  const { closeModal } = useModalContext();
  const [isChecked, setIsChecked] = useState(false);

  const handleClickDismissButton: React.MouseEventHandler<HTMLButtonElement> = (e) => {
    onClickDismissButton?.(e);
    closeModal();
  };

  const handleClickConfirmButton: React.MouseEventHandler<HTMLButtonElement> = (e) => {
    onClickConfirmButton(e);
    closeModal();
  };

  return (
    <ModalWindow title="지원서 미리보기" className={styles.box}>
      <div className={styles.content}>
        {recruitmentItems.map((recruitmentItem, index) => (
          <div key={index} className={styles["application-item-box"]}>
            <h3 className={styles["application-item-title"]}>
              {recruitmentItem.position}. {recruitmentItem.title}
            </h3>
            <span className={styles["application-item-description"]}>
              {recruitmentItem.description}
            </span>
            <p className={styles["application-item-answer"]}>{answers[index]}</p>
          </div>
        ))}

        <div className={styles["application-item-box"]}>
          <h3 className={styles["application-item-title"]}>URL</h3>
          <a
            className={styles["application-item-url"]}
            target="_blank"
            rel="noopener noreferrer"
            href={referenceUrl}
          >
            {referenceUrl}
          </a>
        </div>
      </div>

      <CheckBox
        name="confirm-submit"
        label="제출한 뒤에는 수정할 수 없음을 확인했습니다."
        checked={isChecked}
        onChange={(e: ChangeEvent) => {
          setIsChecked((e.target as HTMLInputElement).checked);
        }}
        required
      />
      <div className={styles["button-box"]}>
        <Button
          type="button"
          variant="contained"
          className={styles.button}
          cancel={true}
          onClick={handleClickDismissButton}
        >
          취소
        </Button>
        <Button
          type="submit"
          variant="contained"
          className={styles.button}
          cancel={false}
          onClick={handleClickConfirmButton}
          disabled={!isChecked}
        >
          제출
        </Button>
      </div>
    </ModalWindow>
  );
};

export default ApplicationPreviewModalWindow;
