import classNames from "classnames";
import Button, { BUTTON_VARIANT } from "../../@common/Button/Button";
import styles from "./ApplicationButtons.module.css";

type ApplyButtonProps = {
  readonly label: string;
  isButtonDisabled: boolean;
  onClick: () => void;
};

const ApplyButton = ({ label, isButtonDisabled, onClick }: ApplyButtonProps) => {
  return (
    <Button
      className={classNames(styles["apply-button"])}
      type="button"
      variant={BUTTON_VARIANT.CONTAINED}
      cancel={false}
      disabled={isButtonDisabled}
      onClick={onClick}
    >
      {label}
    </Button>
  );
};

export default ApplyButton;
