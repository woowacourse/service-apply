import { useEffect } from "react";
import { TextField } from "../../components/form";
import useFormContext from "../../hooks/useFormContext";
import PropTypes from "prop-types";

const InputField = ({ name, initialValue, type, ...props }) => {
  const {
    value,
    errorMessage,
    handleChange,
    handleKeyUp,
    register,
    unRegister,
  } = useFormContext();

  useEffect(() => {
    register(name, initialValue);

    return () => {
      unRegister(name);
    };
  }, [name, initialValue]);

  return (
    <TextField
      name={name}
      type={type ?? "text"}
      value={value[name]}
      errorMessage={errorMessage[name]}
      onChange={handleChange}
      onKeyUp={type === "password" ? handleKeyUp : null}
      {...props}
    />
  );
};

export default InputField;

InputField.propTypes = {
  initialValue: PropTypes.string,
  name: PropTypes.string,
};
