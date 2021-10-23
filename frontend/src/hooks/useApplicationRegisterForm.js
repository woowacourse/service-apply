import { useEffect, useState } from "react";
import { generatePath, useHistory } from "react-router";
import * as Api from "../api";
import { ERROR_MESSAGE } from "../constants/messages";
import PATH, { PARAM } from "../constants/path";
import FORM from "../constants/form";
import { formatDateTime } from "../utils/format/date";
import { generateQuery } from "../utils/route/query";
import { isValidURL } from "../utils/validation/url";
import useTokenContext from "./useTokenContext";

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
  const [requiredForm, setRequiredForm] = useState(
    requiredFormInitialValue(recruitmentItems.length)
  );
  const [form, setForm] = useState(formInitialValue);
  const [errorMessage, setErrorMessage] = useState(errorMessageInitialValue);

  const [modifiedDateTime, setModifiedDateTime] = useState("");
  const history = useHistory();
  const { token } = useTokenContext();

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

  const handleInitError = (error) => {
    if (!error) return;

    if (error.response?.status === 409) {
      alert(error.response?.data.message);
      history.push(PATH.HOME);
      return;
    }

    history.replace({
      pathname: generatePath(PATH.APPLICATION_FORM, {
        status: PARAM.APPLICATION_FORM_STATUS.EDIT,
      }),
      search: generateQuery({ recruitmentId }),
      state: { currentRecruitment },
    });
  };

  const handleLoadFormError = (error) => {
    if (!error) return;

    // TODO: 서버 에러응답을 클라이언트에서 분기처리하여 메시지 표시한다.
    alert(error.response.data.message);
    history.push(PATH.RECRUITS);
  };

  const loadForm = async () => {
    try {
      const { data } = await Api.fetchForm({ token, recruitmentId });
      const { answers, referenceUrl, modifiedDateTime } = data;

      setRequiredForm({ answers: answers.map((answer) => answer.contents) });
      setForm({ referenceUrl });
      setModifiedDateTime(formatDateTime(new Date(modifiedDateTime)));
    } catch (error) {
      handleLoadFormError(error);
    }
  };

  const createForm = async () => {
    try {
      await Api.createForm({ token, recruitmentId });
    } catch (error) {
      handleInitError(error);
    }
  };

  const init = async () => {
    if (status === PARAM.APPLICATION_FORM_STATUS.EDIT) loadForm();
    else createForm();
  };

  useEffect(() => {
    if (!recruitmentId || !currentRecruitment) {
      history.replace(PATH.RECRUITS);

      return;
    }

    init();
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
    isEmpty,
    isValid,
    reset,
  };
};

export default useApplicationRegisterForm;
