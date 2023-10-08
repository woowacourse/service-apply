import { useState } from "react";
import { ERROR_MESSAGE } from "../constants/messages";
import { isValidGithubUsername } from "../utils/validation/githubUsername";
import { isValidPullRequestUrl } from "../utils/validation/pullRequestUrl";
import { isValidRepositoryUrl } from "../utils/validation/repositoryUrl";
import { MISSION_SUBMISSION_METHOD } from "../constants/recruitment";

export const ASSIGNMENT_FORM_NAME = {
  GITHUB_USERNAME: "githubUsername",
  URL: "pullRequestUrl", // TODO: pullRequestUrl -> url로 변경
  NOTE: "note",
};

const initialRequiredForm = {
  [ASSIGNMENT_FORM_NAME.GITHUB_USERNAME]: "",
  [ASSIGNMENT_FORM_NAME.URL]: "",
  [ASSIGNMENT_FORM_NAME.NOTE]: "",
};

const initialErrorMessage = {
  [ASSIGNMENT_FORM_NAME.URL]: "",
};

const useAssignmentForm = (submissionMethod) => {
  const [requiredForm, setRequiredForm] = useState(initialRequiredForm);
  const [errorMessage, setErrorMessage] = useState(initialErrorMessage);

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

  const handleChangeGithubUsername = ({ target }) => {
    const errorMessage = isValidGithubUsername(target.value)
      ? ""
      : ERROR_MESSAGE.VALIDATION.GITHUB_USERNAME;

    updateErrorMessage(ASSIGNMENT_FORM_NAME.GITHUB_USERNAME, errorMessage);
    updateRequiredForm(ASSIGNMENT_FORM_NAME.GITHUB_USERNAME, target.value);
  };

  const validateUrl = (url, submissionMethod) => {
    if (submissionMethod === MISSION_SUBMISSION_METHOD.PRIVATE_REPOSITORY) {
      return isValidRepositoryUrl(url);
    }

    return isValidPullRequestUrl(url);
  };

  const generateErrorMessage = (url, submissionMethod) => {
    if (validateUrl(url, submissionMethod)) {
      return "";
    }

    if (submissionMethod === MISSION_SUBMISSION_METHOD.PRIVATE_REPOSITORY) {
      return ERROR_MESSAGE.VALIDATION.REPOSITORY_URL;
    }

    return ERROR_MESSAGE.VALIDATION.PULL_REQUEST_URL;
  };

  const handleChangeUrl = ({ target }) => {
    const errorMessage = generateErrorMessage(target.value, submissionMethod);

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
      [ASSIGNMENT_FORM_NAME.GITHUB_USERNAME]: handleChangeGithubUsername,
      [ASSIGNMENT_FORM_NAME.URL]: handleChangeUrl,
      [ASSIGNMENT_FORM_NAME.NOTE]: handleChangeNote,
    },
    isValid,
    isEmpty,
  };
};

export default useAssignmentForm;
