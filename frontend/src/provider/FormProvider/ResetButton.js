import { Button } from "../../components/form";
import useFormContext from "../../hooks/useFormContext";
import PropTypes from "prop-types";

const ResetButton = ({ children }) => {
  const { reset } = useFormContext();

  return (
    <Button
      type="reset"
      onClick={() => {
        if (window.confirm("정말 초기화하시겠습니까?")) {
          reset();
        }
      }}
    >
      {children}
    </Button>
  );
};

export default ResetButton;

ResetButton.propTypes = {
  children: PropTypes.node.isRequired,
};
