import classNames from "classnames";
import Button, { BUTTON_VARIANT } from "../../@common/Button/Button";
import styles from "./ApplicationButtons.module.css";

type JudgmentButtonProps = {
  disabled?: boolean;
  onClick?: () => void;
};

const JudgmentButton = ({ disabled, onClick }: JudgmentButtonProps) => {
  return (
    <Button
      className={classNames(styles["judgment-button"])}
      type="button"
      variant={BUTTON_VARIANT.CONTAINED}
      cancel={false}
      disabled={disabled || false}
      onClick={() => onClick?.()}
    >
      예제 테스트 실행
    </Button>
  );
};

export default JudgmentButton;
