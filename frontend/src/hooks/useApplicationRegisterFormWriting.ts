import { useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { ERROR_MESSAGE, SUCCESS_MESSAGE } from "../constants/messages";
import { PATH } from "../constants/path";
import * as Api from "../api";
import { formatDateTime } from "../utils/format/date";
import useTokenContext from "./useTokenContext";
import useModalContext from "./useModalContext";
import { Answer, UnprocessedApplicationForm } from "../../types/domains/applicationForms";
import { RecruitmentItem } from "../../types/domains/recruitments";
import { AxiosError } from "axios";

type ApiError = AxiosError<Api.FetchFormErrorResponseData>;
type UpdateFormAnswersArgument = {
  modifiedDateTime: string;
  setModifiedDateTime: React.Dispatch<React.SetStateAction<string>>;
  recruitmentItems: RecruitmentItem[];
  recruitmentId: number;
  form: UnprocessedApplicationForm; //{ [key: string]: string | boolean | string[] };
};

type CreateFormArgument = { token: string; recruitmentId: number };
type UpsertAnswersArgument = {
  isNewApplication: boolean;
  token: string;
  data: { recruitmentId: number; referenceUrl: string; answers: Answer[]; submitted: boolean };
};

const createForm = async ({ token, recruitmentId }: CreateFormArgument) => {
  await Api.createForm({ token, recruitmentId });
};

const upsertAnswers = async ({
  isNewApplication = false,
  token,
  data: { recruitmentId, referenceUrl, answers, submitted = false },
}: UpsertAnswersArgument) => {
  if (isNewApplication) {
    await createForm({ token, recruitmentId });
  }

  await Api.updateForm({
    token,
    data: {
      recruitmentId,
      referenceUrl,
      answers,
      submitted,
    },
  });
};

const useApplicationRegisterFormWriting = ({
  modifiedDateTime,
  setModifiedDateTime,
  recruitmentItems,
  recruitmentId,
  form,
}: UpdateFormAnswersArgument) => {
  const navigate = useNavigate();
  const { token } = useTokenContext();
  const { openModal } = useModalContext();

  const handleRequestError = useCallback((error: ApiError) => {
    if (!error) return;

    if (error.response?.status === 409) {
      alert(error.response?.data.message);
      navigate(PATH.HOME);
      return;
    }

    alert(ERROR_MESSAGE.API.SAVE_APPLICATION_FORM);
  }, []);

  const combineAnswers = useCallback(
    (answers: string[]) =>
      recruitmentItems.map((item, index) => ({
        contents: answers[index] ?? "",
        recruitmentItemId: item.id,
      })),
    [recruitmentItems]
  );

  const updateFormAnswers = useCallback(
    async (submitted = false) => {
      try {
        const { referenceUrl, answers } = form;

        await upsertAnswers({
          isNewApplication: !modifiedDateTime,
          token,
          data: {
            recruitmentId,
            referenceUrl: referenceUrl as string,
            answers: combineAnswers(answers as string[]) as Answer[],
            submitted,
          },
        });

        if (submitted) {
          alert(SUCCESS_MESSAGE.API.SUBMIT_ASSIGNMENT);
          navigate(PATH.HOME, { replace: true });
          return;
        }

        alert(SUCCESS_MESSAGE.API.SAVE_APPLICATION);
        setModifiedDateTime(formatDateTime(new Date()));
      } catch (error) {
        handleRequestError(error as ApiError);
      }
    },
    [form, token, modifiedDateTime, setModifiedDateTime, handleRequestError]
  );

  const handleSubmit = useCallback(
    (event: React.FormEvent) => {
      event.preventDefault();

      openModal();
    },
    [openModal]
  );

  return {
    updateFormAnswers,
    handleSubmit,
  };
};

export default useApplicationRegisterFormWriting;
