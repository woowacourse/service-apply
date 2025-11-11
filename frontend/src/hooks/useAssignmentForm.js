import { useMemo, useState } from "react";
import { ERROR_MESSAGE } from "../constants/messages";
import { isValidPullRequestUrl } from "../utils/validation/pullRequestUrl";
import { isValidRepositoryUrl } from "../utils/validation/repositoryUrl";
import { isValidUrl } from "../utils/validation/url";
import { MISSION_SUBMISSION_METHOD } from "../constants/recruitment";

export const ASSIGNMENT_FORM_NAME = {
  URL: "url",
  NOTE: "note",
};

const initialRequiredForm = {
  [ASSIGNMENT_FORM_NAME.URL]: "",
  [ASSIGNMENT_FORM_NAME.NOTE]: "",
};

const initialErrorMessage = {
  [ASSIGNMENT_FORM_NAME.URL]: "",
};

const VALIDATORS = {
  [MISSION_SUBMISSION_METHOD.PUBLIC_PULL_REQUEST]: {
    test: isValidPullRequestUrl,
    error: ERROR_MESSAGE.VALIDATION.PULL_REQUEST_URL,
  },
  [MISSION_SUBMISSION_METHOD.PRIVATE_REPOSITORY]: {
    test: isValidRepositoryUrl,
    error: ERROR_MESSAGE.VALIDATION.REPOSITORY_URL,
  },
  default: {
    test: isValidUrl,
    error: ERROR_MESSAGE.VALIDATION.URL,
  },
};

const useAssignmentForm = (submissionMethod = MISSION_SUBMISSION_METHOD.PUBLIC_PULL_REQUEST) => {
  const [requiredForm, setRequiredForm] = useState(initialRequiredForm);
  const [errorMessage, setErrorMessage] = useState(initialErrorMessage);

  const validator = useMemo(
    () => VALIDATORS[submissionMethod] ?? VALIDATORS.default,
    [submissionMethod]
  );

  const isValid = Object.values(errorMessage).filter(Boolean).length === 0;
  const isEmpty =
    Object.values(requiredForm)
      .map((value) => (typeof value === "string" ? value.trimEnd() : value))
      .filter(Boolean).length < Object.keys(requiredForm).length;

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

  const validateAndMessage = (url) => {
    const value = typeof url === "string" ? url.trim() : url;
    return validator.test(value) ? "" : validator.error;
  };

  const handleChangeUrl = ({ target }) => {
    const errorMessage = validateAndMessage(target.value);

    updateErrorMessage(ASSIGNMENT_FORM_NAME.URL, errorMessage);
    updateRequiredForm(ASSIGNMENT_FORM_NAME.URL, target.value);
  };

  const handleChangeNote = ({ target }) => {
    updateRequiredForm(ASSIGNMENT_FORM_NAME.NOTE, target.value);
  };

  return {
    form: requiredForm,
    errorMessage,
    init,
    handleChanges: {
      [ASSIGNMENT_FORM_NAME.URL]: handleChangeUrl,
      [ASSIGNMENT_FORM_NAME.NOTE]: handleChangeNote,
    },
    isValid,
    isEmpty,
  };
};

export default useAssignmentForm;
