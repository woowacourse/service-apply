import React, { useEffect, useState } from "react";
import * as Api from "../api";
import { RECRUITMENT_STATUS } from "../constants/recruitment";
import { RecruitmentContext } from "../hooks/useRecruitmentContext";

export const recruitmentFilter = (recruitments) => ({
  all: recruitments,
  recruitable: recruitments.filter(
    ({ status }) => status === RECRUITMENT_STATUS.RECRUITABLE
  ),
  recruiting: recruitments.filter(({ status }) =>
    [RECRUITMENT_STATUS.RECRUITING, RECRUITMENT_STATUS.UNRECRUITABLE].includes(
      status
    )
  ),
  ended: recruitments.filter(
    ({ status }) => status === RECRUITMENT_STATUS.ENDED
  ),
  findById: (recruitmentId) =>
    recruitments.find(({ id }) => id === recruitmentId),
});

const RecruitmentProvider = ({ children }) => {
  const [recruitments, setRecruitments] = useState([]);

  useEffect(() => {
    try {
      const fetchData = async () => {
        const { data: recruitments } = await Api.fetchRecruitments();

        setRecruitments(recruitments.sort((a, b) => b.id - a.id));
      };

      fetchData();
    } catch (e) {
      console.error(e);
      setRecruitments([]);
    }
  }, []);

  return (
    <RecruitmentContext.Provider
      value={{
        recruitment: recruitmentFilter(recruitments),
      }}
    >
      {children}
    </RecruitmentContext.Provider>
  );
};

export default RecruitmentProvider;
