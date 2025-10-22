import { useState } from "react";
import { ERROR_MESSAGE } from "../constants/messages";

export const LOGIN_FORM_NAME = {
  EMAIL: "email",
  PASSWORD: "password",
};

const useLoginForm = () => {
  const [requiredForm, setRequiredForm] = useState({
    [LOGIN_FORM_NAME.EMAIL]: "",
    [LOGIN_FORM_NAME.PASSWORD]: "",
  });

  const [errorMessage, setErrorMessage] = useState({
    [LOGIN_FORM_NAME.PASSWORD]: "",
  });

  const isEmpty =
    Object.values(requiredForm).filter(Boolean).length < Object.keys(requiredForm).length;

  const handleEmailChange = ({ target }) => {
    setRequiredForm((prev) => ({
      ...prev,
      [LOGIN_FORM_NAME.EMAIL]: target.value,
    }));
  };

  const handlePasswordChange = ({ target }) => {
    setRequiredForm((prev) => ({
      ...prev,
      [LOGIN_FORM_NAME.PASSWORD]: target.value,
    }));
  };

  const handleCapsLockState = (name) => (event) => {
    const newErrorMessage = event.getModifierState("CapsLock")
      ? ERROR_MESSAGE.VALIDATION.PASSWORD_CAPS_LOCK
      : "";

    setErrorMessage((prev) => ({
      ...prev,
      [name]: newErrorMessage,
    }));
  };

  return {
    form: requiredForm,
    errorMessage,
    handleChanges: {
      [LOGIN_FORM_NAME.EMAIL]: handleEmailChange,
      [LOGIN_FORM_NAME.PASSWORD]: handlePasswordChange,
    },
    handleCapsLockState,
    isEmpty,
  };
};

export default useLoginForm;
