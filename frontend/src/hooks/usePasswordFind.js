import { useState } from "react";
import { ERROR_MESSAGE } from "../constants/messages";
import { isValidEmail } from "../utils/validation/email";
import { isValidName } from "../utils/validation/name";

export const PASSWORD_FIND_FORM = {
  NAME: "name",
  EMAIL: "email",
  BIRTHDAY: "birthday",
};

const initialRequiredForm = {
  [PASSWORD_FIND_FORM.NAME]: "",
  [PASSWORD_FIND_FORM.EMAIL]: "",
  [PASSWORD_FIND_FORM.BIRTHDAY]: "",
};

const initialErrorMessage = {
  [PASSWORD_FIND_FORM.NAME]: "",
  [PASSWORD_FIND_FORM.EMAIL]: "",
};

const usePasswordFindForm = () => {
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

  const handleChangeName = ({ target }) => {
    const errorMessage = isValidName(target.value)
      ? ""
      : ERROR_MESSAGE.VALIDATION.NAME;

    updateErrorMessage(PASSWORD_FIND_FORM.NAME, errorMessage);
    updateRequiredForm(PASSWORD_FIND_FORM.NAME, target.value);
  };

  const handleChangeEmail = ({ target }) => {
    const errorMessage = isValidEmail(target.value)
      ? ""
      : ERROR_MESSAGE.VALIDATION.EMAIL;

    updateErrorMessage(PASSWORD_FIND_FORM.EMAIL, errorMessage);
    updateRequiredForm(PASSWORD_FIND_FORM.EMAIL, target.value);
  };

  const handleChangeBirthday = ({ target }) => {
    updateRequiredForm(PASSWORD_FIND_FORM.BIRTHDAY, target.value);
  };

  return {
    form: requiredForm,
    errorMessage,
    handleChange: {
      [PASSWORD_FIND_FORM.NAME]: handleChangeName,
      [PASSWORD_FIND_FORM.EMAIL]: handleChangeEmail,
      [PASSWORD_FIND_FORM.BIRTHDAY]: handleChangeBirthday,
    },
    isValid,
    isEmpty,
  };
};

export default usePasswordFindForm;
