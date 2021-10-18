import { useState } from "react";
import { ERROR_MESSAGE } from "../constants/messages";

export const LOGIN_FORM = {
  EMAIL: "email",
  PASSWORD: "password",
};

const useLoginForm = () => {
  const [requiredForm, setRequiredForm] = useState({
    [LOGIN_FORM.EMAIL]: "",
    [LOGIN_FORM.PASSWORD]: "",
  });

  const [errorMessage, setErrorMessage] = useState({
    [LOGIN_FORM.PASSWORD]: "",
  });

  const isEmpty =
    Object.values(requiredForm).filter(Boolean).length <
    Object.keys(requiredForm).length;

  const handleEmailChange = ({ target }) => {
    setRequiredForm((prev) => ({
      ...prev,
      [LOGIN_FORM.EMAIL]: target.value,
    }));
  };

  const handlePasswordChange = ({ target }) => {
    setRequiredForm((prev) => ({
      ...prev,
      [LOGIN_FORM.PASSWORD]: target.value,
    }));
  };

  const handleCapsLockState = (name) => (event) => {
    const newErrorMessage = event.getModifierState("CapsLock")
      ? ERROR_MESSAGE.VALIDATION.PASSWORD_CAPSLOCK
      : "";

    setErrorMessage((prev) => ({
      ...prev,
      [name]: newErrorMessage,
    }));
  };

  return {
    form: requiredForm,
    errorMessage,
    handleChange: {
      [LOGIN_FORM.EMAIL]: handleEmailChange,
      [LOGIN_FORM.PASSWORD]: handlePasswordChange,
    },
    handleCapsLockState,
    isEmpty,
  };
};

export default useLoginForm;
