import { useCallback } from "react";
import { generatePath, useNavigate } from "react-router-dom";
import { AxiosError } from "axios";
import useTokenContext from "./useTokenContext";
import useRecruitList from "./useRecruitList";
import { ERROR_MESSAGE } from "../constants/messages";
import { ERROR_CODE } from "../constants/errorCodes";
import { PATH } from "../constants/path";
import { Recruitment } from "../../types/domains/recruitments";
import { ApplicationForm } from "../../types/domains/applicationForms";
import { generateQuery } from "../utils/route/query";
import * as Api from "../api";

type ApiError = AxiosError<Api.FetchFormErrorResponseData>;
type NavigateState = { application?: ApplicationForm };

type NavigateToApplicationArguments = {
  recruitment: Recruitment;
  state?: NavigateState;
};
type HandleFetchingErrorArguments = { error: ApiError; recruitment: Recruitment };

const useRecruits = () => {
  const { token } = useTokenContext();
  const navigate = useNavigate();

  const { programTabStatus, setProgramTabStatus, filteredRecruitments, isRecruitable } =
    useRecruitList();

  const navigateToApplication = useCallback(
    ({ recruitment, state }: NavigateToApplicationArguments) => {
      navigate(
        {
          pathname: generatePath(PATH.APPLICATION_FORM),
          search: generateQuery({ recruitmentId: recruitment.id.toString() }),
        },
        { state: { currentRecruitment: recruitment, ...state } }
      );
    },
    []
  );

  const handleFetchingError = useCallback(
    ({ error, recruitment }: HandleFetchingErrorArguments) => {
      // TODO: useApplicationRegisterForm 오류 핸들링과 공용화 시도하기
      if (!error) return;

      const status = error?.response?.status;
      const message = error?.response?.data?.message;

      if (status === ERROR_CODE.LOAD_APPLICATION_FORM.NOT_FOUND) {
        navigateToApplication({ recruitment });
        return;
      }

      if (status === ERROR_CODE.LOAD_APPLICATION_FORM.ALREADY_APPLIED && message) {
        alert(message);
        return;
      }

      alert(ERROR_MESSAGE.API.LOAD_APPLICATION_FORM);
      navigate(PATH.HOME, { replace: true });
    },
    []
  );

  const fetchMyApplication = useCallback(
    async (recruitment: Recruitment) => {
      try {
        if (!isRecruitable(recruitment)) {
          alert("지원 불가능한 모집입니다.");
          return;
        }

        const { data: application } = await Api.fetchForm({
          token,
          recruitmentId: recruitment.id,
        });

        navigateToApplication({
          recruitment,
          state: { application },
        });
      } catch (error) {
        handleFetchingError({
          error: error as ApiError,
          recruitment,
        });
      }
    },
    [isRecruitable]
  );

  return {
    token,
    tabs: {
      programTabStatus,
      setProgramTabStatus,
      filteredRecruitments,
    },
    fetchMyApplication,
  };
};

export default useRecruits;
