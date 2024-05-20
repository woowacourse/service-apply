import { useState } from "react";
import { ERROR_MESSAGE } from "../constants/messages";
import { formatHyphen, PHONE_NUMBER_HYPHEN_IDX } from "../utils/format/phoneNumber";
import { isValidEmail } from "../utils/validation/email";
import { isValidGithubUsername } from "../utils/validation/githubUsername";
import { isValidName } from "../utils/validation/name";
import { isValidPassword } from "../utils/validation/password";
import { isValidPhoneNumber } from "../utils/validation/phoneNumber";

export const SIGN_UP_FORM_NAME = {
  IS_TERM_AGREED: "isTermAgreed",
  EMAIL: "email",
  AUTHENTICATION_CODE: "authenticationCode",
  PASSWORD: "password",
  CONFIRM_PASSWORD: "confirmPassword",
  NAME: "name",
  BIRTHDAY: "birthday",
  PHONE_NUMBER: "phoneNumber",
  GITHUB_USERNAME: "githubUsername",
};

const initialRequiredForm = {
  [SIGN_UP_FORM_NAME.IS_TERM_AGREED]: false,
  [SIGN_UP_FORM_NAME.EMAIL]: "",
  [SIGN_UP_FORM_NAME.AUTHENTICATION_CODE]: "",
  [SIGN_UP_FORM_NAME.PASSWORD]: "",
  [SIGN_UP_FORM_NAME.CONFIRM_PASSWORD]: "",
  [SIGN_UP_FORM_NAME.NAME]: "",
  [SIGN_UP_FORM_NAME.BIRTHDAY]: null,
  [SIGN_UP_FORM_NAME.PHONE_NUMBER]: "",
  [SIGN_UP_FORM_NAME.GITHUB_USERNAME]: "",
};

const initialErrorMessage = {
  [SIGN_UP_FORM_NAME.EMAIL]: "",
  [SIGN_UP_FORM_NAME.AUTHENTICATION_CODE]: "",
  [SIGN_UP_FORM_NAME.PASSWORD]: "",
  [SIGN_UP_FORM_NAME.CONFIRM_PASSWORD]: "",
  [SIGN_UP_FORM_NAME.NAME]: "",
  [SIGN_UP_FORM_NAME.PHONE_NUMBER]: "",
  [SIGN_UP_FORM_NAME.GITHUB_USERNAME]: "",
};

const useSignUpForm = () => {
  const [requiredForm, setRequiredForm] = useState(initialRequiredForm);
  const [errorMessage, setErrorMessage] = useState(initialErrorMessage);

  const isValid =
    Object.values(errorMessage).filter(
      (errMsg) => errMsg && errMsg !== ERROR_MESSAGE.VALIDATION.PASSWORD_CAPSLOCK
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

  const handleChangeIsTermAgreed = ({ target }) => {
    updateRequiredForm(SIGN_UP_FORM_NAME.IS_TERM_AGREED, target.checked);
  };

  const handleChangeEmail = ({ target }) => {
    const errorMessage = isValidEmail(target.value) ? "" : ERROR_MESSAGE.VALIDATION.EMAIL;

    updateErrorMessage(SIGN_UP_FORM_NAME.EMAIL, errorMessage);
    updateRequiredForm(SIGN_UP_FORM_NAME.EMAIL, target.value);
  };

  const handleChangeAuthenticationCode = ({ target }) => {
    const result = target.value.replaceAll(" ", "");

    updateRequiredForm(SIGN_UP_FORM_NAME.AUTHENTICATION_CODE, result);
  };

  const handleChangePassword = ({ target }) => {
    const errorMessage = isValidPassword(target.value) ? "" : ERROR_MESSAGE.VALIDATION.PASSWORD;

    const confirmPasswordErrorMessage =
      !requiredForm.confirmPassword || target.value === requiredForm.confirmPassword
        ? ""
        : ERROR_MESSAGE.VALIDATION.CONFIRM_PASSWORD;

    updateErrorMessage(SIGN_UP_FORM_NAME.CONFIRM_PASSWORD, confirmPasswordErrorMessage);
    updateErrorMessage(SIGN_UP_FORM_NAME.PASSWORD, errorMessage);
    updateRequiredForm(SIGN_UP_FORM_NAME.PASSWORD, target.value);
  };

  const handleChangeConfirmPassword = ({ target }) => {
    const errorMessage =
      target.value === requiredForm.password ? "" : ERROR_MESSAGE.VALIDATION.CONFIRM_PASSWORD;

    updateErrorMessage(SIGN_UP_FORM_NAME.CONFIRM_PASSWORD, errorMessage);
    updateRequiredForm(SIGN_UP_FORM_NAME.CONFIRM_PASSWORD, target.value);
  };

  const handleChangeName = ({ target }) => {
    const errorMessage = isValidName(target.value) ? "" : ERROR_MESSAGE.VALIDATION.NAME;

    updateErrorMessage(SIGN_UP_FORM_NAME.NAME, errorMessage);
    updateRequiredForm(SIGN_UP_FORM_NAME.NAME, target.value);
  };

  const handleChangeBirthday = (date) => {
    updateRequiredForm(SIGN_UP_FORM_NAME.BIRTHDAY, date);
  };

  const handleChangePhoneNumber = ({ nativeEvent: { data }, target: { value } }) => {
    if (isNaN(data)) return;

    const [firstHyphenIdx, secondHyphenIdx] = PHONE_NUMBER_HYPHEN_IDX;
    const result = formatHyphen(value, firstHyphenIdx, secondHyphenIdx).trim();

    const errorMessage = isValidPhoneNumber(result) ? "" : ERROR_MESSAGE.VALIDATION.PHONE_NUMBER;

    updateErrorMessage(SIGN_UP_FORM_NAME.PHONE_NUMBER, errorMessage);
    updateRequiredForm(SIGN_UP_FORM_NAME.PHONE_NUMBER, result);
  };

  const handleChangeGithubUsername = ({ target }) => {
    updateRequiredForm(SIGN_UP_FORM_NAME.GITHUB_USERNAME, target.value);

    const errorMessage = isValidGithubUsername(target.value)
      ? ""
      : ERROR_MESSAGE.VALIDATION.GITHUB_USERNAME;
    updateErrorMessage(SIGN_UP_FORM_NAME.GITHUB_USERNAME, errorMessage);
  };

  const handleCapsLockState = (name) => (event) => {
    const newErrorMessage = event.getModifierState("CapsLock")
      ? ERROR_MESSAGE.VALIDATION.PASSWORD_CAPSLOCK
      : errorMessage[name];

    updateErrorMessage(name, newErrorMessage);
  };

  const reset = (name) => {
    if (!name) {
      setRequiredForm(initialRequiredForm);
      setErrorMessage(initialErrorMessage);

      return;
    }

    setRequiredForm((prev) => ({
      ...prev,
      [name]: initialRequiredForm[name],
    }));
    setErrorMessage((prev) => ({
      ...prev,
      [name]: initialErrorMessage[name],
    }));
  };

  return {
    form: requiredForm,
    errorMessage,
    isValid,
    isEmpty,
    reset,
    setErrorMessage: updateErrorMessage,
    handleCapsLockState,
    handleChanges: {
      [SIGN_UP_FORM_NAME.IS_TERM_AGREED]: handleChangeIsTermAgreed,
      [SIGN_UP_FORM_NAME.EMAIL]: handleChangeEmail,
      [SIGN_UP_FORM_NAME.AUTHENTICATION_CODE]: handleChangeAuthenticationCode,
      [SIGN_UP_FORM_NAME.PASSWORD]: handleChangePassword,
      [SIGN_UP_FORM_NAME.CONFIRM_PASSWORD]: handleChangeConfirmPassword,
      [SIGN_UP_FORM_NAME.NAME]: handleChangeName,
      [SIGN_UP_FORM_NAME.BIRTHDAY]: handleChangeBirthday,
      [SIGN_UP_FORM_NAME.PHONE_NUMBER]: handleChangePhoneNumber,
      [SIGN_UP_FORM_NAME.GITHUB_USERNAME]: handleChangeGithubUsername,
    },
  };
};

export default useSignUpForm;
