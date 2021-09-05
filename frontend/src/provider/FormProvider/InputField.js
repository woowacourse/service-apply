import { useEffect } from "react";
import { TextField } from "../../components/form";
import useFormContext from "../../hooks/useFormContext";
import PropTypes from "prop-types";

const InputField = ({ name, initialValue, ...props }) => {
  const { value, errorMessage, onChange, register, unRegister } =
    useFormContext();

  useEffect(() => {
    register(name, initialValue);

    return () => {
      unRegister(name);
    };
  }, []);

  return (
    <TextField
      name={name}
      value={value[name]}
      errorMessage={errorMessage[name]}
      onChange={onChange}
      {...props}
    />
  );
};

export default InputField;

InputField.propTypes = {
  initialValue: PropTypes.string,
  name: PropTypes.string,
};
