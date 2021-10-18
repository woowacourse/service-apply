import Button, { BUTTON_VARIANT } from '../../@common/Button/Button';

const CancelButton = ({ onClick }) => {
  return (
    <Button type="button" variant={BUTTON_VARIANT.OUTLINED} onClick={onClick}>
      취소
    </Button>
  );
};

export default CancelButton;
