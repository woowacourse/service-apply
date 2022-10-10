import { ChangeEvent, useState } from "react";
import useModalContext from "../../hooks/useModalContext";
import Button, { BUTTON_VARIANT } from "../@common/Button/Button";
import styles from "./ApplicationPreviewModal.module.css";
import CheckBox from "../form/CheckBox/CheckBox";
import { RecruitmentItem } from "../../../types/domains/recruitments";
import { Answer, ApplicationForm } from "../../../types/domains/applicationForms";

type ApplicationPreviewModalWindowProps = {
  recruitmentItems: RecruitmentItem[];
  answers: Answer["contents"][];
  referenceUrl: ApplicationForm["referenceUrl"];
  onClickConfirmButton: React.MouseEventHandler<HTMLButtonElement>;
};

const ApplicationPreviewModal = ({
  recruitmentItems,
  answers,
  referenceUrl,
  onClickConfirmButton,
}: ApplicationPreviewModalWindowProps) => {
  const { closeModal } = useModalContext();
  const [isChecked, setIsChecked] = useState(false);

  const handleChangeConfirmCheckbox: React.FormEventHandler<HTMLInputElement> = (e) => {
    setIsChecked((e.target as HTMLInputElement).checked);
  };

  const handleClickDismissButton: React.MouseEventHandler<HTMLButtonElement> = () => {
    closeModal();
  };

  const handleClickConfirmButton: React.MouseEventHandler<HTMLButtonElement> = (e) => {
    onClickConfirmButton(e);
    closeModal();
  };

  return (
    <div className={styles.box}>
      <h2 id="dialogTitle" className={styles.title}>
        지원서 미리보기
      </h2>
      <div className={styles.content}>
        {recruitmentItems.map((recruitmentItem, index) => (
          <div key={recruitmentItem.id} className={styles["application-item-box"]}>
            <h3 className={styles["application-item-title"]}>
              {recruitmentItem.position}. {recruitmentItem.title}
            </h3>
            <span className={styles["application-item-description"]}>
              {recruitmentItem.description}
            </span>
            <span className={styles["application-item-answer-length"]}>
              {answers[index].length} / {recruitmentItem.maximumLength}
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
        onChange={handleChangeConfirmCheckbox}
        autoFocus
        required
      />
      <div className={styles["button-box"]}>
        <Button
          type="button"
          variant={BUTTON_VARIANT.CONTAINED}
          className={styles.button}
          cancel={true}
          onClick={handleClickDismissButton}
        >
          취소
        </Button>
        <Button
          type="submit"
          variant={BUTTON_VARIANT.CONTAINED}
          className={styles.button}
          cancel={false}
          onClick={handleClickConfirmButton}
          disabled={!isChecked}
        >
          제출
        </Button>
      </div>
    </div>
  );
};

export default ApplicationPreviewModal;
