import { useEffect } from "react";
import { TextField } from "../../components/form";
import useFormContext from "../../hooks/useFormContext";
import PropTypes from "prop-types";

const InputField = ({ name, initialValue, onChange, ...props }) => {
  const { value, errorMessage, handleChange, register, unRegister } =
    useFormContext();

  useEffect(() => {
    register(name, initialValue);

    return () => {
      unRegister(name);
    };
  }, [name, initialValue]);

  return (
    <TextField
      name={name}
      value={value[name]}
      errorMessage={errorMessage[name]}
      onChange={onChange ?? handleChange}
      {...props}
    />
  );
};

export default InputField;

InputField.propTypes = {
  initialValue: PropTypes.string,
  name: PropTypes.string,
};
