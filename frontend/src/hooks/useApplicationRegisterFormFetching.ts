import { useCallback, useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router";
import * as Api from "../api";
import { FORM } from "../constants/form";
import { ERROR_MESSAGE } from "../constants/messages";
import { PATH } from "../constants/path";
import { formatDateTime } from "../utils/format/date";
import { isValidURL } from "../utils/validation/url";
import useTokenContext from "./useTokenContext";
import { ERROR_CODE } from "../constants/errorCodes";
import { RecruitmentItem } from "../../types/domains/recruitments";
import { AxiosError } from "axios";
import { UnprocessedApplicationForm } from "../../types/domains/applicationForms";
import { APPLICATION_REGISTER_FORM_NAME } from "../constants/application";

type ApplicationRegisterFormArgument = {
  recruitmentId: number;
  recruitmentItems: RecruitmentItem[];
};

const generateFormAnswerInitialValue = (answerCount: number): UnprocessedApplicationForm => ({
  [APPLICATION_REGISTER_FORM_NAME.ANSWERS]: Array.from({ length: answerCount }, () => ""),
  [APPLICATION_REGISTER_FORM_NAME.IS_TERM_AGREED]: false,
  [APPLICATION_REGISTER_FORM_NAME.REFERENCE_URL]: "",
});

const errorMessageInitialValue: UnprocessedApplicationForm = {
  [APPLICATION_REGISTER_FORM_NAME.REFERENCE_URL]: "",
};

function isInputRequirementSatisfied(array: string[], requiredCount: number): boolean {
  const nonEmptyItems = array.filter((item = "") => item.trim() !== "");
  return nonEmptyItems.length >= requiredCount;
}

const useApplicationRegisterForm = ({
  recruitmentId,
  recruitmentItems = [],
}: ApplicationRegisterFormArgument) => {
  const [form, setForm] = useState<UnprocessedApplicationForm>({
    ...generateFormAnswerInitialValue(recruitmentItems.length),
  });

  const [errorMessage, setErrorMessage] = useState(errorMessageInitialValue);

  const [modifiedDateTime, setModifiedDateTime] = useState("");
  const navigate = useNavigate();
  const { token } = useTokenContext();

  const isAnswersEmpty = useMemo(
    () => form?.answers && !isInputRequirementSatisfied(form.answers, recruitmentItems.length),
    [form, recruitmentItems]
  );

  const isEmpty = useMemo(() => isAnswersEmpty || !form?.isTermAgreed, [isAnswersEmpty, form]);

  const isValid = useMemo(
    () => Object.values(errorMessage).filter(Boolean).length === 0,
    [errorMessage]
  );

  const handleChangeAnswer = useCallback(
    (index: number) =>
      ({ target }: React.ChangeEvent<HTMLTextAreaElement>) => {
        if (recruitmentItems[index].maximumLength < target.value.length) return;

        const newAnswers = form.answers?.slice() || [];
        newAnswers[index] = target.value;

        setForm((prev) => ({
          ...prev,
          [APPLICATION_REGISTER_FORM_NAME.ANSWERS]: newAnswers,
        }));
      },
    [recruitmentItems, form]
  );

  const updateErrorMessage = useCallback(
    (name: string, errorMessage: string) => {
      setErrorMessage((prev) => ({ ...prev, [name]: errorMessage }));
    },
    [errorMessage]
  );

  const handleChangeReferenceUrl = useCallback(
    ({ target }: React.ChangeEvent<HTMLInputElement>) => {
      if (target.value.length > FORM.REFERENCE_URL_MAX_LENGTH) return;

      const errorMessage = isValidURL(target.value) ? "" : ERROR_MESSAGE.VALIDATION.URL;

      updateErrorMessage(APPLICATION_REGISTER_FORM_NAME.REFERENCE_URL, errorMessage);
      setForm((prev) => ({
        ...prev,
        [APPLICATION_REGISTER_FORM_NAME.REFERENCE_URL]: target.value,
      }));
    },
    [errorMessage, form]
  );

  const handleChangeIsTermAgreed = useCallback(
    ({ target }: React.ChangeEvent<HTMLInputElement>) => {
      setForm((prev) => ({
        ...prev,
        [APPLICATION_REGISTER_FORM_NAME.IS_TERM_AGREED]: target.checked,
      }));
    },
    [form]
  );

  const handleFetchingError = useCallback((error: AxiosError<Api.FetchFormErrorResponseData>) => {
    if (!error) return;

    const status = error?.response?.status;
    const message = error?.response?.data?.message;

    if (status === ERROR_CODE.LOAD_APPLICATION_FORM.NOT_FOUND) {
      return;
    }

    if (status === ERROR_CODE.LOAD_APPLICATION_FORM.ALREADY_APPLIED && message) {
      alert(message);
      navigate(PATH.RECRUITS);
      return;
    }

    alert(ERROR_MESSAGE.API.LOAD_APPLICATION_FORM);
    navigate(PATH.RECRUITS);
  }, []);

  const loadForm = useCallback(async () => {
    try {
      const application = (await Api.fetchForm({ token, recruitmentId }))?.data;
      const { answers = [], referenceUrl, modifiedDateTime } = application;

      const requiredAnswers = answers?.map(({ contents }) => contents);
      setForm({ referenceUrl, answers: requiredAnswers });
      setModifiedDateTime(formatDateTime(new Date(modifiedDateTime)));
    } catch (error) {
      handleFetchingError(error as AxiosError<Api.FetchFormErrorResponseData>);
    }
  }, [form, modifiedDateTime]);

  useEffect(() => {
    if (!recruitmentId) {
      navigate(PATH.RECRUITS, { replace: true });
      return;
    }

    loadForm();
  }, []);

  const reset = useCallback(() => {
    setForm({ ...generateFormAnswerInitialValue(recruitmentItems.length) });
    setErrorMessage(errorMessageInitialValue);
  }, [recruitmentItems]);

  return {
    isEmpty,
    isValid,
    form: { ...form },
    errorMessage,

    modifiedDateTime,
    setModifiedDateTime,

    reset,
    handleChanges: {
      [APPLICATION_REGISTER_FORM_NAME.ANSWERS]: handleChangeAnswer,
      [APPLICATION_REGISTER_FORM_NAME.REFERENCE_URL]: handleChangeReferenceUrl,
      [APPLICATION_REGISTER_FORM_NAME.IS_TERM_AGREED]: handleChangeIsTermAgreed,
    },
  };
};

export default useApplicationRegisterForm;
