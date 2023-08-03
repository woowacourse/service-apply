import { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router";
import * as Api from "../api";
import { FORM } from "../constants/form";
import { ERROR_MESSAGE } from "../constants/messages";
import { PATH } from "../constants/path";
import { formatDateTime } from "../utils/format/date";
import { isValidURL } from "../utils/validation/url";
import useTokenContext from "./useTokenContext";
import { ERROR_CODE } from "../constants/errorCodes";

export const APPLICATION_REGISTER_FORM_NAME = {
  ANSWERS: "answers",
  REFERENCE_URL: "referenceUrl",
  IS_TERM_AGREED: "isTermAgreed",
};

const requiredFormInitialValue = (answerCount) => ({
  [APPLICATION_REGISTER_FORM_NAME.ANSWERS]: Array(answerCount).fill(""),
  [APPLICATION_REGISTER_FORM_NAME.IS_TERM_AGREED]: false,
});

const formInitialValue = {
  [APPLICATION_REGISTER_FORM_NAME.REFERENCE_URL]: "",
};

const errorMessageInitialValue = {
  [APPLICATION_REGISTER_FORM_NAME.REFERENCE_URL]: "",
};

const useApplicationRegisterForm = ({
  recruitmentId,
  currentRecruitment,
  recruitmentItems = [],
  status,
}) => {
  /*
    TODO: requiredForm, form 상태를 통합해도 괜찮지 않을까?
    return { form: { ...form, ...requiredForm }, ... } 을 하고 있으니...
  */
  const [requiredForm, setRequiredForm] = useState(
    requiredFormInitialValue(recruitmentItems.length)
  );
  const [form, setForm] = useState(formInitialValue);

  const [errorMessage, setErrorMessage] = useState(errorMessageInitialValue);

  const [modifiedDateTime, setModifiedDateTime] = useState("");
  const navigate = useNavigate();
  const location = useLocation();
  const { token } = useTokenContext();

  const [isNewApplication, setIsNewApplication] = useState(false);

  const isAnswersEmpty =
    requiredForm[APPLICATION_REGISTER_FORM_NAME.ANSWERS]
      .map((value = "") => value.trimEnd())
      .filter(Boolean).length < recruitmentItems.length;

  const isEmpty = isAnswersEmpty || !requiredForm[APPLICATION_REGISTER_FORM_NAME.IS_TERM_AGREED];

  const isValid = Object.values(errorMessage).filter(Boolean).length === 0;

  const handleChangeAnswer =
    (index) =>
    ({ target }) => {
      if (recruitmentItems[index].maximumLength < target.value.length) return;

      const newAnswers = requiredForm[APPLICATION_REGISTER_FORM_NAME.ANSWERS].slice();

      newAnswers[index] = target.value;

      setRequiredForm((prev) => ({
        ...prev,
        [APPLICATION_REGISTER_FORM_NAME.ANSWERS]: newAnswers,
      }));
    };

  const updateErrorMessage = (name, errorMessage) => {
    setErrorMessage((prev) => ({ ...prev, [name]: errorMessage }));
  };

  const handleChangeReferenceUrl = ({ target }) => {
    if (target.value.length > FORM.REFERENCE_URL) return;

    const errorMessage = isValidURL(target.value) ? "" : ERROR_MESSAGE.VALIDATION.URL;

    updateErrorMessage(APPLICATION_REGISTER_FORM_NAME.REFERENCE_URL, errorMessage);
    setForm((prev) => ({
      ...prev,
      [APPLICATION_REGISTER_FORM_NAME.REFERENCE_URL]: target.value,
    }));
  };

  const handleChangeIsTermAgreed = ({ target }) => {
    setRequiredForm((prev) => ({
      ...prev,
      [APPLICATION_REGISTER_FORM_NAME.IS_TERM_AGREED]: target.checked,
    }));
  };

  const handleFetchingError = (error) => {
    if (!error) return;

    const status = error?.response?.status;
    const message = error?.response?.data?.message;

    if (status === ERROR_CODE.LOAD_APPLICATION_FORM.NOT_FOUND) {
      setIsNewApplication(true);
      return;
    }

    if (status === ERROR_CODE.LOAD_APPLICATION_FORM.ALREADY_APPLIED && message) {
      alert(message);
      navigate(PATH.RECRUITS);
      return;
    }

    alert(ERROR_MESSAGE.API.LOAD_APPLICATION_FORM);
    navigate(PATH.RECRUITS);
  };

  const loadForm = async () => {
    try {
      const application =
        location?.state?.application ?? (await Api.fetchForm({ token, recruitmentId }))?.data;
      const { answers = [], referenceUrl, modifiedDateTime } = application;

      const requiredAnswers = answers?.map(({ contents }) => contents);
      setRequiredForm({ answers: requiredAnswers });
      setForm({ referenceUrl });
      setModifiedDateTime(formatDateTime(new Date(modifiedDateTime)));
    } catch (error) {
      handleFetchingError(error);
    }
  };

  useEffect(() => {
    if (!recruitmentId || !currentRecruitment) {
      navigate(PATH.RECRUITS, { replace: true });
      return;
    }

    loadForm();
  }, [status]);

  const reset = () => {
    setRequiredForm(requiredFormInitialValue);
    setForm(formInitialValue);
    setErrorMessage(errorMessageInitialValue);
  };

  return {
    form: { ...form, ...requiredForm },
    errorMessage,
    handleChanges: {
      [APPLICATION_REGISTER_FORM_NAME.ANSWERS]: handleChangeAnswer,
      [APPLICATION_REGISTER_FORM_NAME.REFERENCE_URL]: handleChangeReferenceUrl,
      [APPLICATION_REGISTER_FORM_NAME.IS_TERM_AGREED]: handleChangeIsTermAgreed,
    },
    modifiedDateTime,
    setModifiedDateTime,
    isNewApplication,
    isEmpty,
    isValid,
    reset,
  };
};

export default useApplicationRegisterForm;
