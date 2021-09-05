import { useState } from "react";
import { Form } from "../../components/form";
import { FormContext } from "../../hooks/useFormContext";
import PropTypes from "prop-types";

const FormProvider = ({ submit, validators, children }) => {
  const [value, setValue] = useState({});
  const [errorMessage, setErrorMessage] = useState({});

  const isValid = Object.values(errorMessage).filter(Boolean).length === 0;
  const isEmpty =
    Object.values(value).filter(Boolean).length < Object.values(value).length;

  const onChange = ({ target }) => {
    setValue((prev) => ({ ...prev, [target.name]: target.value }));

    const validator = validators?.[target.name];
    if (!validator) return;

    try {
      const result = validator(target.value);
      if (typeof result === "function") {
        result(value);
      }

      setErrorMessage((prev) => ({ ...prev, [target.name]: null }));
    } catch (error) {
      setErrorMessage((prev) => ({ ...prev, [target.name]: error.message }));
    }
  };

  const onSubmit = (event) => {
    event.preventDefault();

    if (!isValid) return;

    submit(value);
  };

  const register = (name) => {
    setValue((prev) => ({ ...prev, [name]: "" }));
    setErrorMessage((prev) => ({ ...prev, [name]: null }));
  };

  const unRegister = (name) => {
    const { [name]: deletedValue, ...newValue } = value;
    const { [name]: deletedErrormessage, ...newErrorMessage } = errorMessage;

    setValue(newValue);
    setErrorMessage(newErrorMessage);
  };

  return (
    <FormContext.Provider
      value={{
        value,
        errorMessage,
        onChange,
        register,
        unRegister,
        isValid,
        isEmpty,
      }}
    >
      <Form onSubmit={onSubmit}>{children}</Form>
    </FormContext.Provider>
  );
};

export default FormProvider;

FormProvider.propTypes = {
  children: PropTypes.node.isRequired,
  submit: PropTypes.func.isRequired,
  validators: PropTypes.object,
};
