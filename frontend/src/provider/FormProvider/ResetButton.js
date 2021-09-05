import PropTypes from "prop-types";
import { Button } from "../../components/form";
import useFormContext from "../../hooks/useFormContext";
import { CONFIRM_MESSAGE } from '../../constants/messages';

const ResetButton = ({ children }) => {
  const { reset } = useFormContext();

  const handleClick = () => {
    if (window.confirm(CONFIRM_MESSAGE.RESET_APPLICATION)) {
      reset();
    }
  };

  return (
    <Button type="reset" onClick={handleClick}>
      {children}
    </Button>
  );
};

export default ResetButton;

ResetButton.propTypes = {
  children: PropTypes.node.isRequired,
};
