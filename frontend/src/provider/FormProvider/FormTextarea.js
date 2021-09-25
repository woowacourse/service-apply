import { useEffect } from "react";
import PropTypes from "prop-types";
import TextareaField from "../../components/form/TextareaField/TextareaField";
import useFormContext from "../../hooks/useFormContext";

const FormTextarea = ({ name, initialValue, ...props }) => {
  const { value, errorMessage, handleChange, register, unRegister } =
    useFormContext();

  useEffect(() => {
    register(name, initialValue);

    return () => {
      unRegister(name);
    };
  }, [name, initialValue]);

  return (
    <TextareaField
      name={name}
      value={value[name]}
      errorMessage={errorMessage[name]}
      onChange={handleChange}
      {...props}
    />
  );
};

export default FormTextarea;

FormTextarea.propTypes = {
  initialValue: PropTypes.string,
  name: PropTypes.string,
};
