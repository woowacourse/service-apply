import { useContext, createContext } from "react";
import { ERROR_MESSAGE } from '../constants/messages';

export const RecruitmentContext = createContext();

const useRecruitmentContext = () => {
  const recruitmentContext = useContext(RecruitmentContext);

  if (!recruitmentContext)
    throw Error(ERROR_MESSAGE.HOOKS.CANNOT_FIND_RECRUITMENT_CONTEXT);

  return recruitmentContext;
};

export default useRecruitmentContext;
