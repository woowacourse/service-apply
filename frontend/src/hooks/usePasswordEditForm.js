import { useState } from "react";
import { ERROR_MESSAGE } from "../constants/messages";
import { isValidPassword } from "../utils/validation/password";

export const PASSWORD_EDIT_FORM_NAME = {
  OLD_PASSWORD: "oldPassword",
  PASSWORD: "password",
  CONFIRM_PASSWORD: "confirmPassword",
};

const initialRequiredForm = {
  [PASSWORD_EDIT_FORM_NAME.OLD_PASSWORD]: "",
  [PASSWORD_EDIT_FORM_NAME.PASSWORD]: "",
  [PASSWORD_EDIT_FORM_NAME.CONFIRM_PASSWORD]: "",
};

const initialErrorMessage = {
  [PASSWORD_EDIT_FORM_NAME.OLD_PASSWORD]: "",
  [PASSWORD_EDIT_FORM_NAME.PASSWORD]: "",
  [PASSWORD_EDIT_FORM_NAME.CONFIRM_PASSWORD]: "",
};

const usePasswordEditForm = () => {
  const [requiredForm, setRequiredForm] = useState(initialRequiredForm);
  const [errorMessage, setErrorMessage] = useState(initialErrorMessage);

  const isValid =
    Object.values(errorMessage).filter(
      (errMsg) => errMsg && errMsg !== ERROR_MESSAGE.VALIDATION.PASSWORD_CAPS_LOCK
    ).length === 0;
  const isEmpty =
    Object.values(requiredForm).filter(Boolean).length < Object.keys(requiredForm).length;

  const updateRequiredForm = (name, value) => {
    setRequiredForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const updateErrorMessage = (name, errorMessage) => {
    setErrorMessage((prev) => ({
      ...prev,
      [name]: errorMessage,
    }));
  };

  const handleChangeOldPassword = ({ target }) => {
    updateRequiredForm(PASSWORD_EDIT_FORM_NAME.OLD_PASSWORD, target.value);
  };

  const handleChangePassword = ({ target }) => {
    const errorMessage = isValidPassword(target.value) ? "" : ERROR_MESSAGE.VALIDATION.PASSWORD;

    const confirmPasswordErrorMessage =
      !requiredForm.confirmPassword || target.value === requiredForm.confirmPassword
        ? ""
        : ERROR_MESSAGE.VALIDATION.CONFIRM_PASSWORD;

    updateErrorMessage(PASSWORD_EDIT_FORM_NAME.CONFIRM_PASSWORD, confirmPasswordErrorMessage);
    updateErrorMessage(PASSWORD_EDIT_FORM_NAME.PASSWORD, errorMessage);
    updateRequiredForm(PASSWORD_EDIT_FORM_NAME.PASSWORD, target.value);
  };

  const handleChangeConfirmPassword = ({ target }) => {
    const errorMessage =
      target.value === requiredForm.password ? "" : ERROR_MESSAGE.VALIDATION.CONFIRM_PASSWORD;

    updateErrorMessage(PASSWORD_EDIT_FORM_NAME.CONFIRM_PASSWORD, errorMessage);
    updateRequiredForm(PASSWORD_EDIT_FORM_NAME.CONFIRM_PASSWORD, target.value);
  };

  const handleCapsLockState = (name) => (event) => {
    const newErrorMessage = event.getModifierState("CapsLock")
      ? ERROR_MESSAGE.VALIDATION.PASSWORD_CAPS_LOCK
      : errorMessage[name];

    updateErrorMessage(name, newErrorMessage);
  };

  return {
    form: requiredForm,
    errorMessage,
    handleCapsLockState,
    handleChanges: {
      [PASSWORD_EDIT_FORM_NAME.OLD_PASSWORD]: handleChangeOldPassword,
      [PASSWORD_EDIT_FORM_NAME.PASSWORD]: handleChangePassword,
      [PASSWORD_EDIT_FORM_NAME.CONFIRM_PASSWORD]: handleChangeConfirmPassword,
    },
    isValid,
    isEmpty,
  };
};

export default usePasswordEditForm;
