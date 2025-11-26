import { useState } from "react";
import { ERROR_MESSAGE } from "../constants/messages";
import { isValidEmail } from "../utils/validation/email";
import { isValidName } from "../utils/validation/name";
import { REGEX } from "../constants/regex";

export const PASSWORD_FIND_FORM_NAME = {
  NAME: "name",
  EMAIL: "email",
  BIRTHDAY: "birthday",
};

const initialRequiredForm = {
  [PASSWORD_FIND_FORM_NAME.NAME]: "",
  [PASSWORD_FIND_FORM_NAME.EMAIL]: "",
  [PASSWORD_FIND_FORM_NAME.BIRTHDAY]: null,
};

const initialErrorMessage = {
  [PASSWORD_FIND_FORM_NAME.NAME]: "",
  [PASSWORD_FIND_FORM_NAME.EMAIL]: "",
};

const usePasswordFindForm = () => {
  const [requiredForm, setRequiredForm] = useState(initialRequiredForm);
  const [errorMessage, setErrorMessage] = useState(initialErrorMessage);

  const isValid = Object.values(errorMessage).filter(Boolean).length === 0;
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

  const handleChangeName = ({ target }) => {
    const result = target.value.replace(REGEX.MULTIPLE_SPACES, " ");

    const errorMessage = isValidName(result) ? "" : ERROR_MESSAGE.VALIDATION.NAME;

    updateErrorMessage(PASSWORD_FIND_FORM_NAME.NAME, errorMessage);
    updateRequiredForm(PASSWORD_FIND_FORM_NAME.NAME, result);
  };

  const handleChangeEmail = ({ target }) => {
    const errorMessage = isValidEmail(target.value) ? "" : ERROR_MESSAGE.VALIDATION.EMAIL;

    updateErrorMessage(PASSWORD_FIND_FORM_NAME.EMAIL, errorMessage);
    updateRequiredForm(PASSWORD_FIND_FORM_NAME.EMAIL, target.value);
  };

  const handleChangeBirthday = (date) => {
    updateRequiredForm(PASSWORD_FIND_FORM_NAME.BIRTHDAY, date);
  };

  return {
    form: requiredForm,
    errorMessage,
    handleChanges: {
      [PASSWORD_FIND_FORM_NAME.NAME]: handleChangeName,
      [PASSWORD_FIND_FORM_NAME.EMAIL]: handleChangeEmail,
      [PASSWORD_FIND_FORM_NAME.BIRTHDAY]: handleChangeBirthday,
    },
    isValid,
    isEmpty,
  };
};

export default usePasswordFindForm;
