import React, { useState, useEffect } from "react";
import { RecruitmentContext } from "../hooks/useRecruitmentContext";
import * as Api from "../api";
import { RECRUITMENT_STATUS } from "../constants/recruitment";

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

  const fetchMyApplicationForms = async (payload) => {
    try {
      const { data: myApplicationForms } = await Api.fetchMyApplicationForms(
        payload
      );

      return myApplicationForms;
    } catch (e) {
      console.error(e);
      throw e;
    }
  };

  return (
    <RecruitmentContext.Provider
      value={{
        recruitment: recruitmentFilter(recruitments),
        fetchMyApplicationForms,
      }}
    >
      {children}
    </RecruitmentContext.Provider>
  );
};

export default RecruitmentProvider;
