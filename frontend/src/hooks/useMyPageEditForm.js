import { useState } from "react";
import { ERROR_MESSAGE } from "../constants/messages";
import { formatHyphen, PHONE_NUMBER_HYPHEN_IDX } from "../utils/format/phoneNumber";
import { isValidPhoneNumber } from "../utils/validation/phoneNumber";

export const MY_PAGE_EDIT_FORM_NAME = {
  EMAIL: "email",
  BIRTHDAY: "birthday",
  PHONE_NUMBER: "phoneNumber",
  GITHUB_USERNAME: "githubUsername",
};

const initialRequiredForm = {
  [MY_PAGE_EDIT_FORM_NAME.PHONE_NUMBER]: "",
};

const initialErrorMessage = {
  [MY_PAGE_EDIT_FORM_NAME.PHONE_NUMBER]: "",
};

const useMyPageEditForm = () => {
  const [requiredForm, setRequiredForm] = useState(initialRequiredForm);
  const [errorMessage, setErrorMessage] = useState(initialErrorMessage);

  const isValid = Object.values(errorMessage).filter(Boolean).length === 0;
  const isEmpty =
    Object.values(requiredForm).filter(Boolean).length < Object.keys(requiredForm).length;

  const init = ({ requiredForm }) => {
    setRequiredForm(requiredForm);
  };

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

  const handleChangePhoneNumber = ({ nativeEvent: { data }, target: { value } }) => {
    if (isNaN(data)) return;

    const [firstHyphenIdx, secondHyphenIdx] = PHONE_NUMBER_HYPHEN_IDX;
    const result = formatHyphen(value, firstHyphenIdx, secondHyphenIdx).trim();

    const errorMessage = isValidPhoneNumber(result) ? "" : ERROR_MESSAGE.VALIDATION.PHONE_NUMBER;

    updateErrorMessage(MY_PAGE_EDIT_FORM_NAME.PHONE_NUMBER, errorMessage);
    updateRequiredForm(MY_PAGE_EDIT_FORM_NAME.PHONE_NUMBER, result);
  };

  return {
    form: requiredForm,
    errorMessage,
    init,
    handleChanges: {
      [MY_PAGE_EDIT_FORM_NAME.PHONE_NUMBER]: handleChangePhoneNumber,
    },
    isValid,
    isEmpty,
  };
};

export default useMyPageEditForm;
