import { useEffect } from "react";
import useFormContext from "../../hooks/useFormContext";
import PropTypes from "prop-types";
import TextInputField from "../../components/form/TextInputField/TextInputField";

const FormInput = ({ name, initialValue, ...props }) => {
  const { value, errorMessage, handleChange, register, unRegister } =
    useFormContext();

  useEffect(() => {
    register(name, initialValue);

    return () => {
      unRegister(name);
    };
  }, [name, initialValue]);

  return (
    <TextInputField
      name={name}
      value={value[name]}
      errorMessage={errorMessage[name]}
      onChange={handleChange}
      {...props}
    />
  );
};

export default FormInput;

FormInput.propTypes = {
  initialValue: PropTypes.string,
  name: PropTypes.string,
};
