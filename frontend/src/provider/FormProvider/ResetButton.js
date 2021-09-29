import { Button } from "../../components/form";
import useFormContext from "../../hooks/useFormContext";
import { CONFIRM_MESSAGE } from "../../constants/messages";

const ResetButton = () => {
  const { reset } = useFormContext();

  const handleClick = () => {
    if (window.confirm(CONFIRM_MESSAGE.RESET_APPLICATION)) {
      reset();
    }
  };

  return (
    <Button type="reset" onClick={handleClick}>
      초기화
    </Button>
  );
};

export default ResetButton;
