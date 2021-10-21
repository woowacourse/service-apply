import PropTypes from "prop-types";
import { useHistory } from "react-router-dom";
import Button, { BUTTON_VARIANT } from "../../@common/Button/Button";

const CancelButton = ({ confirmMessage }) => {
  const history = useHistory();

  const handleClick = () => {
    if (!confirmMessage || (confirmMessage && window.confirm(confirmMessage))) {
      history.goBack();
    }
  };

  return (
    <Button type="button" onClick={handleClick} variant={BUTTON_VARIANT.OUTLINED}>
      취소
    </Button>
  );
};

CancelButton.propTypes = {
  confirmMessage: PropTypes.string,
};

export default CancelButton;
