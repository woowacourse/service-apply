import { useState } from "react";
import { ERROR_MESSAGE } from "../constants/messages";
import { isValidPassword } from "../utils/validation/password";

export const PASSWORD_EDIT_FORM = {
  OLD_PASSWORD: "oldPassword",
  PASSWORD: "password",
  CONFIRM_PASSWORD: "confirmPassword",
};

const initialRequiredForm = {
  [PASSWORD_EDIT_FORM.OLD_PASSWORD]: "",
  [PASSWORD_EDIT_FORM.PASSWORD]: "",
  [PASSWORD_EDIT_FORM.CONFIRM_PASSWORD]: "",
};

const initialErrorMessage = {
  [PASSWORD_EDIT_FORM.PASSWORD]: "",
  [PASSWORD_EDIT_FORM.CONFIRM_PASSWORD]: "",
};

const usePasswordEditForm = () => {
  const [requiredForm, setRequiredForm] = useState(initialRequiredForm);
  const [errorMessage, setErrorMessage] = useState(initialErrorMessage);

  const isValid = Object.values(errorMessage).filter(Boolean).length === 0;
  const isEmpty =
    Object.values(requiredForm).filter(Boolean).length <
    Object.keys(requiredForm).length;

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
    updateRequiredForm(PASSWORD_EDIT_FORM.OLD_PASSWORD, target.value);
  };

  const handleChangePassword = ({ target }) => {
    const errorMessage = isValidPassword(target.value)
      ? ""
      : ERROR_MESSAGE.VALIDATION.PASSWORD;

    const confirmPasswordErrorMessage =
      target.value === requiredForm.confirmPassword
        ? ""
        : ERROR_MESSAGE.VALIDATION.CONFIRM_PASSWORD;

    updateErrorMessage(
      PASSWORD_EDIT_FORM.CONFIRM_PASSWORD,
      confirmPasswordErrorMessage
    );
    updateErrorMessage(PASSWORD_EDIT_FORM.PASSWORD, errorMessage);
    updateRequiredForm(PASSWORD_EDIT_FORM.PASSWORD, target.value);
  };

  const handleChangeConfirmPassword = ({ target }) => {
    const errorMessage =
      target.value === requiredForm.password
        ? ""
        : ERROR_MESSAGE.VALIDATION.CONFIRM_PASSWORD;

    updateErrorMessage(PASSWORD_EDIT_FORM.CONFIRM_PASSWORD, errorMessage);
    updateRequiredForm(PASSWORD_EDIT_FORM.CONFIRM_PASSWORD, target.value);
  };

  return {
    form: requiredForm,
    errorMessage,
    handleChange: {
      [PASSWORD_EDIT_FORM.OLD_PASSWORD]: handleChangeOldPassword,
      [PASSWORD_EDIT_FORM.PASSWORD]: handleChangePassword,
      [PASSWORD_EDIT_FORM.CONFIRM_PASSWORD]: handleChangeConfirmPassword,
    },
    isValid,
    isEmpty,
  };
};

export default usePasswordEditForm;
