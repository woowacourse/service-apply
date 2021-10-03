import useFormContext from "../../../hooks/useFormContext";
import { CONFIRM_MESSAGE } from "../../../constants/messages";
import Button from "../../@common/Button/Button";
import styles from "./ResetButton.module.css";

const ResetButton = () => {
  const { reset } = useFormContext();

  const handleClick = () => {
    if (window.confirm(CONFIRM_MESSAGE.RESET_APPLICATION)) {
      reset();
    }
  };

  return (
    <Button
      type="reset"
      onClick={handleClick}
      className={styles["reset-button"]}
    >
      초기화
    </Button>
  );
};

export default ResetButton;
