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
import { Recruitment, RecruitmentItem } from "../../types/domains/recruitments";
import { AxiosError } from "axios";
import { ApplicationForm } from "../../types/domains/applicationForms";

export const APPLICATION_REGISTER_FORM_NAME = {
  ANSWERS: "answers" as const,
  REFERENCE_URL: "referenceUrl" as const,
  IS_TERM_AGREED: "isTermAgreed" as const,
};

type ApplicationRegisterFormArgument = {
  recruitmentId: number;
  currentRecruitment: Recruitment;
  recruitmentItems: RecruitmentItem[];
};

type UnprocessedApplicationForm = {
  [APPLICATION_REGISTER_FORM_NAME.ANSWERS]?: string[];
  [APPLICATION_REGISTER_FORM_NAME.IS_TERM_AGREED]?: boolean;
  [APPLICATION_REGISTER_FORM_NAME.REFERENCE_URL]: string;
};

const generateFormAnswerInitialValue = (answerCount: number): UnprocessedApplicationForm => ({
  [APPLICATION_REGISTER_FORM_NAME.ANSWERS]: Array.from({ length: answerCount }, () => ""),
  [APPLICATION_REGISTER_FORM_NAME.IS_TERM_AGREED]: false,
  [APPLICATION_REGISTER_FORM_NAME.REFERENCE_URL]: "",
});

const errorMessageInitialValue: UnprocessedApplicationForm = {
  [APPLICATION_REGISTER_FORM_NAME.REFERENCE_URL]: "",
};

const useApplicationRegisterForm = ({
  recruitmentId,
  currentRecruitment,
  recruitmentItems = [],
}: ApplicationRegisterFormArgument) => {
  /*
    TODO: requiredForm, form 상태를 통합해도 괜찮지 않을까?
    return { form: { ...form, ...requiredForm }, ... } 을 하고 있으니...
  */
  const [form, setForm] = useState<UnprocessedApplicationForm>({
    ...generateFormAnswerInitialValue(recruitmentItems.length),
  });

  const [errorMessage, setErrorMessage] = useState(errorMessageInitialValue);

  const [modifiedDateTime, setModifiedDateTime] = useState("");
  const navigate = useNavigate();
  const location = useLocation();
  const { token } = useTokenContext();

  const isAnswersEmpty =
    form?.answers &&
    form.answers.map((value = "") => value.trimEnd()).filter(Boolean).length <
      recruitmentItems.length;

  const isEmpty = isAnswersEmpty || !form?.isTermAgreed;

  const isValid = Object.values(errorMessage).filter(Boolean).length === 0;

  const handleChangeAnswer =
    (index: number) =>
    ({ target }: React.ChangeEvent<HTMLTextAreaElement>) => {
      if (recruitmentItems[index].maximumLength < target.value.length) return;

      const newAnswers = form.answers?.slice() || [];

      newAnswers[index] = target.value;

      setForm((prev) => ({
        ...prev,
        [APPLICATION_REGISTER_FORM_NAME.ANSWERS]: newAnswers,
      }));
    };

  const updateErrorMessage = (name: string, errorMessage: string) => {
    setErrorMessage((prev) => ({ ...prev, [name]: errorMessage }));
  };

  const handleChangeReferenceUrl = ({ target }: React.ChangeEvent<HTMLInputElement>) => {
    if (target.value.length > FORM.REFERENCE_URL_MAX_LENGTH) return;

    const errorMessage = isValidURL(target.value) ? "" : ERROR_MESSAGE.VALIDATION.URL;

    updateErrorMessage(APPLICATION_REGISTER_FORM_NAME.REFERENCE_URL, errorMessage);
    setForm((prev) => ({
      ...prev,
      [APPLICATION_REGISTER_FORM_NAME.REFERENCE_URL]: target.value,
    }));
  };

  const handleChangeIsTermAgreed = ({ target }: React.ChangeEvent<HTMLInputElement>) => {
    setForm((prev) => ({
      ...prev,
      [APPLICATION_REGISTER_FORM_NAME.IS_TERM_AGREED]: target.checked,
    }));
  };

  const handleFetchingError = (error: AxiosError<Api.FetchFormErrorResponseData>) => {
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
  };

  const loadForm = async () => {
    try {
      const { application: stateApplication } = location.state as { application: ApplicationForm };
      const application = stateApplication ?? (await Api.fetchForm({ token, recruitmentId }))?.data;
      const { answers = [], referenceUrl, modifiedDateTime } = application;

      const requiredAnswers = answers?.map(({ contents }) => contents);
      setForm({ referenceUrl, answers: requiredAnswers });
      setModifiedDateTime(formatDateTime(new Date(modifiedDateTime)));
    } catch (error) {
      handleFetchingError(error as AxiosError<Api.FetchFormErrorResponseData>);
    }
  };

  useEffect(() => {
    if (!recruitmentId || !currentRecruitment) {
      navigate(PATH.RECRUITS, { replace: true });
      return;
    }

    loadForm();
  }, []);

  const reset = () => {
    setForm({ ...generateFormAnswerInitialValue(recruitmentItems.length) });
    setErrorMessage(errorMessageInitialValue);
  };

  return {
    form: { ...form },
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
