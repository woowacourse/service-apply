import { useContext, createContext } from "react";

export const RecruitmentContext = createContext();

const useRecruitmentContext = () => {
  const recruitmentContext = useContext(RecruitmentContext);

  if (!recruitmentContext)
    throw Error("recruitmentContext가 존재하지 않습니다");

  return recruitmentContext;
};

export default useRecruitmentContext;
