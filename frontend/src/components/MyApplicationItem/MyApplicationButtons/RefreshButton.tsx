import classNames from "classnames";
import Button, { BUTTON_VARIANT } from "../../@common/Button/Button";
import styles from "./ApplicationButtons.module.css";

type RefreshButtonProps = {
  onClick?: () => void;
};

const RefreshButton = ({ onClick }: RefreshButtonProps) => {
  return (
    <Button
      className={classNames(styles["refresh-button"])}
      type="button"
      variant={BUTTON_VARIANT.CONTAINED}
      cancel={false}
      onClick={onClick}
    >
      새로고침
    </Button>
  );
};

export default RefreshButton;
