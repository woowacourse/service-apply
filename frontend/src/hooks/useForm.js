import { useState } from "react";

import { ERROR_MESSAGE } from "../constants/messages";

const useForm = ({ validators, submit }) => {
  const [value, setValue] = useState({});
  const [errorMessage, setErrorMessage] = useState({});

  const isValid = Object.values(errorMessage).filter(Boolean).length === 0;
  const isEmpty =
    Object.values(value).filter(Boolean).length < Object.values(value).length;

  const handleChange = ({ target }) => {
    setValue((prev) => ({
      ...prev,
      [target.name]: target.type === "checkbox" ? target.checked : target.value,
    }));

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

  const handleCapsLockState = (event) => {
    if (event.target.type !== "password") return;

    setErrorMessage((prev) => ({
      ...prev,
      [event.target.name]: event.getModifierState("CapsLock")
        ? ERROR_MESSAGE.VALIDATION.PASSWORD_CAPSLOCK
        : prev[event.target.name],
    }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    if (!isValid) return;

    submit(value);
  };

  const register = (name, initialValue = "") => {
    setValue((prev) => ({ ...prev, [name]: initialValue }));
    setErrorMessage((prev) => ({ ...prev, [name]: null }));
  };

  const unRegister = (name) => {
    const { [name]: deletedValue, ...newValue } = value;
    const { [name]: deletedErrormessage, ...newErrorMessage } = errorMessage;

    setValue(newValue);
    setErrorMessage(newErrorMessage);
  };

  const reset = () => {
    setValue((prev) => {
      for (const val in prev) {
        prev[val] = "";
      }

      return { ...prev };
    });

    setErrorMessage((prev) => {
      for (const val in prev) {
        prev[val] = null;
      }

      return { ...prev };
    });
  };

  return {
    value,
    errorMessage,
    isValid,
    isEmpty,
    handleChange,
    handleCapsLockState,
    handleSubmit,
    register,
    unRegister,
    reset,
  };
};

export default useForm;
