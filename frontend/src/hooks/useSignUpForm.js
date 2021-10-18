import { useState } from "react";
import FORM from "../constants/form";
import { ERROR_MESSAGE } from "../constants/messages";
import { formatHyphen } from "../utils/format/phoneNumber";
import { isValidEmail } from "../utils/validation/email";
import { isValidName } from "../utils/validation/name";
import { isValidPassword } from "../utils/validation/password";
import { isValidPhoneNumber } from "../utils/validation/phoneNumber";

export const SIGN_UP_FORM = {
  EMAIL: "email",
  AUTHENTICATION_CODE: "authenticationCode",
  NAME: "name",
  PHONE_NUMBER: "phoneNumber",
  PASSWORD: "password",
  CONFIRM_PASSWORD: "confirmPassword",
  BIRTH: "birthday",
  GENDER: "gender",
  IS_TERM_AGREED: "isTermAgreed",
};

const initialRequiredForm = {
  [SIGN_UP_FORM.EMAIL]: "",
  [SIGN_UP_FORM.AUTHENTICATION_CODE]: "",
  [SIGN_UP_FORM.NAME]: "",
  [SIGN_UP_FORM.PHONE_NUMBER]: "",
  [SIGN_UP_FORM.PASSWORD]: "",
  [SIGN_UP_FORM.CONFIRM_PASSWORD]: "",
  [SIGN_UP_FORM.BIRTH]: "",
  [SIGN_UP_FORM.GENDER]: "",
  [SIGN_UP_FORM.IS_TERM_AGREED]: false,
};

const initialErrorMessage = {
  [SIGN_UP_FORM.EMAIL]: "",
  [SIGN_UP_FORM.AUTHENTICATION_CODE]: "",
  [SIGN_UP_FORM.NAME]: "",
  [SIGN_UP_FORM.PHONE_NUMBER]: "",
  [SIGN_UP_FORM.PASSWORD]: "",
  [SIGN_UP_FORM.CONFIRM_PASSWORD]: "",
};

const PHONE_NUMBER_HYPHEN_IDX = [3, 7];

const useSignUpForm = () => {
  const [requiredForm, setRequiredForm] = useState(initialRequiredForm);
  const [errorMessage, setErrorMessage] = useState(initialErrorMessage);

  const isValid = Object.values(errorMessage).filter(Boolean).length === 0;
  const isEmpty = Object.values(requiredForm).filter(Boolean).length === 0;

  const updateForm = (name, value) => {
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

  const handleChangeEmail = ({ target }) => {
    const errorMessage = isValidEmail(target.value)
      ? ""
      : ERROR_MESSAGE.VALIDATION.EMAIL;

    updateErrorMessage(SIGN_UP_FORM.EMAIL, errorMessage);
    updateForm(SIGN_UP_FORM.EMAIL, target.value);
  };

  const handleChangeAuthenticationCode = ({ target }) => {
    updateForm(SIGN_UP_FORM.AUTHENTICATION_CODE, target.value);
  };

  const handleChangeName = ({ target }) => {
    const errorMessage = isValidName(target.value)
      ? ""
      : ERROR_MESSAGE.VALIDATION.NAME;

    updateErrorMessage(SIGN_UP_FORM.NAME, errorMessage);
    updateForm(SIGN_UP_FORM.NAME, target.value);
  };

  const handleChangePhoneNumber = ({
    nativeEvent: { data },
    target: { value },
  }) => {
    if (Number.isNaN(data) || value.length > FORM.PHONE_NUMBER_MAX_LENGTH)
      return;

    const [firstHyphenIdx, secondHyphenIdx] = PHONE_NUMBER_HYPHEN_IDX;
    const result = formatHyphen(value, firstHyphenIdx, secondHyphenIdx).trim();

    const errorMessage = isValidPhoneNumber(result)
      ? ""
      : ERROR_MESSAGE.VALIDATION.PHONE_NUMBER;

    updateErrorMessage(SIGN_UP_FORM.PHONE_NUMBER, errorMessage);
    updateForm(SIGN_UP_FORM.PHONE_NUMBER, result);
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
      SIGN_UP_FORM.CONFIRM_PASSWORD,
      confirmPasswordErrorMessage
    );
    updateErrorMessage(SIGN_UP_FORM.PASSWORD, errorMessage);
    updateForm(SIGN_UP_FORM.PASSWORD, target.value);
  };

  const handleChangeConfirmPassword = ({ target }) => {
    const errorMessage =
      target.value === requiredForm.password
        ? ""
        : ERROR_MESSAGE.VALIDATION.CONFIRM_PASSWORD;

    updateErrorMessage(SIGN_UP_FORM.CONFIRM_PASSWORD, errorMessage);
    updateForm(SIGN_UP_FORM.CONFIRM_PASSWORD, target.value);
  };

  const handleChangeBirth = ({ target }) => {
    updateForm(SIGN_UP_FORM.BIRTH, target.value);
  };

  const handleChangeGender = ({ target }) => {
    updateForm(SIGN_UP_FORM.GENDER, target.value);
  };

  const handleChangeIsTermAgreed = ({ target }) => {
    updateForm(SIGN_UP_FORM.IS_TERM_AGREED, target.checked);
  };

  const handleCapsLockState = (event) => {
    const newErrorMessage = event.getModifierState("CapsLock")
      ? ERROR_MESSAGE.VALIDATION.PASSWORD_CAPSLOCK
      : errorMessage[event.target.name];

    updateErrorMessage(event.target.name, newErrorMessage);
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
    handleChange: {
      [SIGN_UP_FORM.IS_TERM_AGREED]: handleChangeIsTermAgreed,
      [SIGN_UP_FORM.EMAIL]: handleChangeEmail,
      [SIGN_UP_FORM.AUTHENTICATION_CODE]: handleChangeAuthenticationCode,
      [SIGN_UP_FORM.NAME]: handleChangeName,
      [SIGN_UP_FORM.PHONE_NUMBER]: handleChangePhoneNumber,
      [SIGN_UP_FORM.PASSWORD]: handleChangePassword,
      [SIGN_UP_FORM.CONFIRM_PASSWORD]: handleChangeConfirmPassword,
      [SIGN_UP_FORM.BIRTH]: handleChangeBirth,
      [SIGN_UP_FORM.GENDER]: handleChangeGender,
    },
  };
};

export default useSignUpForm;
