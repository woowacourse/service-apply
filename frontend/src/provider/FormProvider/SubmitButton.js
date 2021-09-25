import useFormContext from "../../hooks/useFormContext";
import PropTypes from "prop-types";
import Button from "../../components/@common/Button/Button";

const SubmitButton = ({ children }) => {
  const { isValid, isEmpty } = useFormContext();

  return (
    <Button type="submit" disabled={!isValid || isEmpty}>
      {children}
    </Button>
  );
};

export default SubmitButton;

SubmitButton.propTypes = {
  children: PropTypes.node.isRequired,
};
