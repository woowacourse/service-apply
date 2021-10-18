import useFormContext from '../../../hooks/useFormContext';
import { CONFIRM_MESSAGE } from '../../../constants/messages';
import Button, { BUTTON_VARIANT } from '../../@common/Button/Button';

const ResetButton = () => {
  const { resetAll } = useFormContext();

  const handleClick = (event) => {
    event.preventDefault();

    if (window.confirm(CONFIRM_MESSAGE.RESET_APPLICATION)) {
      resetAll();
    }
  };

  return (
    <Button type="reset" variant={BUTTON_VARIANT.OUTLINED} onClick={handleClick}>
      초기화
    </Button>
  );
};

export default ResetButton;
