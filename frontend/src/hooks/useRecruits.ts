import { generatePath, useNavigate } from "react-router-dom";
import { AxiosError } from "axios";
import useTokenContext from "./useTokenContext";
import useRecruitList from "./useRecruitList";
import { ERROR_MESSAGE } from "../constants/messages";
import { ERROR_CODE } from "../constants/errorCodes";
import { PATH, PARAM } from "../constants/path";
import { Recruitment } from "../../types/domains/recruitments";
import { ApplicationForm, ApplicationStatus } from "../../types/domains/applicationForms";
import { generateQuery } from "../utils/route/query";
import * as Api from "../api";

type ApiError = AxiosError<Api.FetchFormErrorResponseData>;
type NavigateState = { application?: ApplicationForm };

type NavigateToApplicationArguments = {
  recruitment: Recruitment;
  status: ApplicationStatus;
  state?: NavigateState;
};
type HandleFetchingErrorArguments = { error: ApiError; recruitment: Recruitment };

const useRecruits = () => {
  const { token } = useTokenContext();
  const navigate = useNavigate();

  const { programTabStatus, setProgramTabStatus, filteredRecruitments, isRecruitable } =
    useRecruitList();

  const navigateToApplication = ({
    recruitment,
    status,
    state,
  }: NavigateToApplicationArguments) => {
    navigate(
      {
        pathname: generatePath(PATH.APPLICATION_FORM, {
          status,
        }),
        search: generateQuery({ recruitmentId: recruitment.id.toString() }),
      },
      { state: { currentRecruitment: recruitment, ...state } }
    );
  };

  const handleFetchingError = ({ error, recruitment }: HandleFetchingErrorArguments) => {
    // TODO: useApplicationRegisterForm 오류 핸들링과 공용화 시도하기
    if (!error) return;

    const status = error?.response?.status;
    const message = error?.response?.data?.message;

    if (status === ERROR_CODE.LOAD_APPLICATION_FORM.NOT_FOUND) {
      navigateToApplication({ recruitment, status: PARAM.APPLICATION_FORM_STATUS.NEW });
      return;
    }

    if (status === ERROR_CODE.LOAD_APPLICATION_FORM.ALREADY_APPLIED && message) {
      alert(message);
      return;
    }

    alert(ERROR_MESSAGE.API.LOAD_APPLICATION_FORM);
    navigate(PATH.HOME, { replace: true });
  };

  const fetchMyApplication = async (recruitment: Recruitment) => {
    try {
      if (!isRecruitable(recruitment)) {
        alert("지원 불가능한 모집입니다.");
        return;
      }

      const { data } = await Api.fetchForm({ token, recruitmentId: recruitment.id.toString() });

      navigateToApplication({
        recruitment,
        status: PARAM.APPLICATION_FORM_STATUS.EDIT,
        state: { application: data },
      });
    } catch (e) {
      const error = e as ApiError;
      handleFetchingError({
        error,
        recruitment,
      });
    }
  };

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
