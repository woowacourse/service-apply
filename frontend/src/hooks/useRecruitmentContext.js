import { useContext, createContext } from "react";

export const RecruitmentContext = createContext();

const useRecruitmentContext = () => {
  const recruitmentContext = useContext(RecruitmentContext);

  if (!recruitmentContext)
    throw Error("recruitmentContext가 존재하지 않습니다");

  const recruitment = {
    all: recruitmentContext.recruitments,
    recruitable: recruitmentContext.recruitments.filter(
      ({ status }) => status === "RECRUITABLE"
    ),
    recruiting: recruitmentContext.recruitments.filter(({ status }) =>
      ["RECRUITING", "UNRECRUITABLE"].includes(status)
    ),
    ended: recruitmentContext.recruitments.filter(
      ({ status }) => status === "ENDED"
    ),
    findById: (recruitmentId) =>
      recruitmentContext.recruitments.find(({ id }) => id === recruitmentId),
  };

  return {
    recruitment,
    fetchMyApplicationForms: recruitmentContext.fetchMyApplicationForms,
  };
};

export default useRecruitmentContext;
