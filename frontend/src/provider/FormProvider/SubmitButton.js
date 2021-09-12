import { Button } from "../../components/form";
import useFormContext from "../../hooks/useFormContext";
import PropTypes from "prop-types";

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
