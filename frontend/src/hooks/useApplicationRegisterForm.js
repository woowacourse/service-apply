import { useEffect, useState } from "react";
import { generatePath, useHistory } from "react-router";
import * as Api from "../api";
import { ERROR_MESSAGE } from "../constants/messages";
import PATH, { PARAM } from "../constants/path";
import { formatDateTime } from "../utils/format/date";
import { generateQuery } from "../utils/route/query";
import { isValidURL } from "../utils/validation/url";
import useTokenContext from "./useTokenContext";

export const APPLICATION_REGISTER_FORM = {
  ANSWERS: "answers",
  REFERENCE_URL: "referenceUrl",
  IS_TERM_AGREED: "isTermAgreed",
};

const RequiredFormInitialValue = {
  [APPLICATION_REGISTER_FORM.ANSWERS]: [],
  [APPLICATION_REGISTER_FORM.IS_TERM_AGREED]: false,
};

const formInitialValue = {
  [APPLICATION_REGISTER_FORM.REFERENCE_URL]: "",
};

const errorMessageInitialValue = {
  [APPLICATION_REGISTER_FORM.REFERENCE_URL]: "",
};

const useApplicationRegisterForm = ({
  recruitmentId,
  currentRecruitment,
  recruitmentItems,
  status,
}) => {
  const [requiredForm, setRequiredForm] = useState(RequiredFormInitialValue);
  const [form, setForm] = useState(formInitialValue);
  const [errorMessage, setErrorMessage] = useState(errorMessageInitialValue);

  const [modifiedDateTime, setModifiedDateTime] = useState("");
  const history = useHistory();
  const { token } = useTokenContext();

  const isAnswersEmpty =
    requiredForm[APPLICATION_REGISTER_FORM.ANSWERS].filter(Boolean).length <
    recruitmentItems.length;

  const isEmpty =
    isAnswersEmpty || !requiredForm[APPLICATION_REGISTER_FORM.IS_TERM_AGREED];

  const isValid = Object.values(errorMessage).filter(Boolean).length === 0;

  const handleChangeAnswer =
    (index) =>
    ({ target }) => {
      const newAnswers = [...requiredForm[APPLICATION_REGISTER_FORM.ANSWERS]];

      newAnswers[index] = target.value;

      setRequiredForm((prev) => ({
        ...prev,
        [APPLICATION_REGISTER_FORM.ANSWERS]: newAnswers,
      }));
    };

  const updateErrorMessage = (name, errorMessage) => {
    setErrorMessage((prev) => ({ ...prev, [name]: errorMessage }));
  };

  const handleChangeReferenceUrl = ({ target }) => {
    const errorMessage = isValidURL(target.value)
      ? ""
      : ERROR_MESSAGE.VALIDATION.URL;

    updateErrorMessage(APPLICATION_REGISTER_FORM.REFERENCE_URL, errorMessage);
    setForm((prev) => ({
      ...prev,
      [APPLICATION_REGISTER_FORM.REFERENCE_URL]: target.value,
    }));
  };

  const handleChangeIsTermAgreed = ({ target }) => {
    setRequiredForm((prev) => ({
      ...prev,
      [APPLICATION_REGISTER_FORM.IS_TERM_AGREED]: target.checked,
    }));
  };

  const loadForm = async () => {
    try {
      const { data } = await Api.fetchForm({ token, recruitmentId });
      const { answers, referenceUrl, modifiedDateTime } = data;

      setRequiredForm({ answers: answers.map((answer) => answer.contents) });
      setForm({ referenceUrl });
      setModifiedDateTime(formatDateTime(new Date(modifiedDateTime)));
    } catch (e) {
      alert(e.response.data.message);
      history.replace(PATH.HOME);
    }
  };

  const createForm = async () => {
    try {
      await Api.createForm({ token, recruitmentId });
    } catch (error) {
      const path = {
        pathname: generatePath(PATH.APPLICATION_FORM, {
          status: PARAM.APPLICATION_FORM_STATUS.EDIT,
        }),
        search: generateQuery({ recruitmentId }),
        state: { currentRecruitment },
      };

      history.replace(path);
    }
  };

  const init = async () => {
    if (status === PARAM.APPLICATION_FORM_STATUS.EDIT) loadForm();
    else createForm();
  };

  useEffect(() => {
    init();
  }, [status]);

  const reset = () => {
    setRequiredForm(RequiredFormInitialValue);
    setForm(formInitialValue);
    setErrorMessage(errorMessageInitialValue);
  };

  return {
    form: { ...form, ...requiredForm },
    errorMessage,
    handleChange: {
      [APPLICATION_REGISTER_FORM.ANSWERS]: handleChangeAnswer,
      [APPLICATION_REGISTER_FORM.REFERENCE_URL]: handleChangeReferenceUrl,
      [APPLICATION_REGISTER_FORM.IS_TERM_AGREED]: handleChangeIsTermAgreed,
    },
    modifiedDateTime,
    setModifiedDateTime,
    isEmpty,
    isValid,
    reset,
  };
};

export default useApplicationRegisterForm;
