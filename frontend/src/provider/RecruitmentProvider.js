import React, { useState, useEffect } from "react";
import { RecruitmentContext } from "../hooks/useRecruitmentContext";
import * as Api from "../api";

const RecruitmentProvider = ({ children }) => {
  const [recruitments, setRecruitments] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      const { data: recruitments } = await Api.fetchRecruitments();

      setRecruitments(recruitments.sort((a, b) => b.id - a.id));
    };

    fetchData();
  }, []);

  const fetchMyApplicationForms = async (payload) => {
    const { data: myApplicationForms } = await Api.fetchMyApplicationForms(
      payload
    );

    return myApplicationForms;
  };

  return (
    <RecruitmentContext.Provider
      value={{ recruitments, fetchMyApplicationForms }}
    >
      {children}
    </RecruitmentContext.Provider>
  );
};

export default RecruitmentProvider;
