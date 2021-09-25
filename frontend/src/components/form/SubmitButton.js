import useFormContext from "../../hooks/useFormContext";
import PropTypes from "prop-types";
import Button from "../@common/Button/Button";

const SubmitButton = ({ children }) => {
  const { isValid, isEmpty } = useFormContext();

  return <Button disabled={!isValid || isEmpty}>{children}</Button>;
};

export default SubmitButton;

SubmitButton.propTypes = {
  children: PropTypes.node.isRequired,
};
