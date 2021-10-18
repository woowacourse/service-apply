import { useState } from "react";
import { ERROR_MESSAGE } from "../constants/messages";
import { isValidPullRequestUrl } from "../utils/validation/pullRequestUrl";

export const ASSIGNMENT_FORM = {
  GITHUB_USERNAME: "githubUsername",
  PULL_REQUEST_URL: "pullRequestUrl",
  NOTE: "note",
};

const initialRequiredForm = {
  [ASSIGNMENT_FORM.GITHUB_USERNAME]: "",
  [ASSIGNMENT_FORM.PULL_REQUEST_URL]: "",
  [ASSIGNMENT_FORM.NOTE]: "",
};

const initialErrorMessage = {
  [ASSIGNMENT_FORM.PULL_REQUEST_URL]: "",
};

const useAssignmentForm = () => {
  const [requiredForm, setRequiredForm] = useState(initialRequiredForm);
  const [errorMessage, setErrorMessage] = useState(initialErrorMessage);

  const isValid = Object.values(errorMessage).filter(Boolean).length === 0;
  const isEmpty =
    Object.values(requiredForm).filter(Boolean).length <
    Object.keys(requiredForm).length;

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
    updateRequiredForm(ASSIGNMENT_FORM.GITHUB_USERNAME, target.value);
  };

  const handleChangePullRequestUrl = ({ target }) => {
    const errorMessage = isValidPullRequestUrl(target.value)
      ? ""
      : ERROR_MESSAGE.VALIDATION.PULL_REQUEST_URL;

    updateErrorMessage(ASSIGNMENT_FORM.PULL_REQUEST_URL, errorMessage);
    updateRequiredForm(ASSIGNMENT_FORM.PULL_REQUEST_URL, target.value);
  };

  const handleChangeNote = ({ target }) => {
    updateRequiredForm(ASSIGNMENT_FORM.NOTE, target.value);
  };

  return {
    form: requiredForm,
    errorMessage,
    init,
    handleChange: {
      [ASSIGNMENT_FORM.GITHUB_USERNAME]: handleChangeGithubUsername,
      [ASSIGNMENT_FORM.PULL_REQUEST_URL]: handleChangePullRequestUrl,
      [ASSIGNMENT_FORM.NOTE]: handleChangeNote,
    },
    isValid,
    isEmpty,
  };
};

export default useAssignmentForm;
