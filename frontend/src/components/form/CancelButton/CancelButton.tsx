import { useNavigate } from "react-router-dom";

import Button, { BUTTON_VARIANT } from "../../@common/Button/Button";
import { ValueOf } from "../../../../types/utility";
import { CONFIRM_MESSAGE } from "../../../constants/messages";

type CancelButtonProps = {
  confirmMessage?: ValueOf<typeof CONFIRM_MESSAGE>;
};

const CancelButton = ({ confirmMessage }: CancelButtonProps) => {
  const navigate = useNavigate();

  const handleClick = () => {
    if (!confirmMessage || (confirmMessage && window.confirm(confirmMessage))) {
      navigate(-1);
    }
  };

  return (
    <Button type="button" onClick={handleClick} variant={BUTTON_VARIANT.OUTLINED}>
      취소
    </Button>
  );
};

export default CancelButton;
