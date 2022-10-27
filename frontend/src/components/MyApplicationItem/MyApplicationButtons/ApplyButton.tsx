import classNames from "classnames";
import Button, { BUTTON_VARIANT } from "../../@common/Button/Button";
import styles from "./ApplicationButtons.module.css";

type ApplyButtonProps = {
  children: Readonly<string>;
  isButtonDisabled: boolean;
  onClick: () => void;
};

const ApplyButton = ({ children, isButtonDisabled, onClick }: ApplyButtonProps) => {
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

export default ApplyButton;
