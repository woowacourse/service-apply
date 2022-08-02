import PropTypes from "prop-types";
import { useNavigate } from "react-router-dom";
import Button, { BUTTON_VARIANT } from "../../@common/Button/Button";

const CancelButton = ({ confirmMessage }) => {
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

CancelButton.propTypes = {
  confirmMessage: PropTypes.string,
};

export default CancelButton;
