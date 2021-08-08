import React, { useState, useEffect } from "react";
import { RecruitmentContext } from "../hooks/useRecruitmentContext";
import * as Api from "../api";
import { RECRUITMENT_STATUS } from "../constants/recruitment";

const RecruitmentProvider = ({ children }) => {
  const [recruitments, setRecruitments] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      const { data: recruitments } = await Api.fetchRecruitments();

      setRecruitments(recruitments.sort((a, b) => b.id - a.id));
    };

    fetchData();
  }, []);

  const recruitmentFilter = {
    all: recruitments,
    recruitable: recruitments.filter(
      ({ status }) => status === RECRUITMENT_STATUS.RECRUITABLE
    ),
    recruiting: recruitments.filter(({ status }) =>
      [
        RECRUITMENT_STATUS.RECRUITING,
        RECRUITMENT_STATUS.UNRECRUITABLE,
      ].includes(status)
    ),
    ended: recruitments.filter(
      ({ status }) => status === RECRUITMENT_STATUS.ENDED
    ),
    findById: (recruitmentId) =>
      recruitments.find(({ id }) => id === recruitmentId),
  };

  const fetchMyApplicationForms = async (payload) => {
    const { data: myApplicationForms } = await Api.fetchMyApplicationForms(
      payload
    );

    return myApplicationForms;
  };

  return (
    <RecruitmentContext.Provider
      value={{ recruitment: recruitmentFilter, fetchMyApplicationForms }}
    >
      {children}
    </RecruitmentContext.Provider>
  );
};

export default RecruitmentProvider;
