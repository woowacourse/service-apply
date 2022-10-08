import { useState, useMemo } from "react";
import useRecruitmentContext from "./useRecruitmentContext";
import { RECRUITS_TAB, COURSE_TAB } from "../constants/tab";
import { Recruitment } from "../../types/domains/recruitments";

const matchProgram = (recruitmentTitle: string, programLabel: string) => {
  const programList = Array.from(Object.values(COURSE_TAB)).sort(
    (a, b) => b.label.length - a.label.length
  );

  const matchingProgram =
    programList.find(({ label }) => {
      return new RegExp(String.raw`^${label}`, "i").test(recruitmentTitle);
    }) ?? COURSE_TAB.ALL;

  return matchingProgram.label === programLabel;
};

const useRecruitList = () => {
  const { recruitment } = useRecruitmentContext();
  const [courseTabStatus, setCourseTabStatus] = useState<
    typeof COURSE_TAB[keyof typeof COURSE_TAB]
  >(COURSE_TAB.ALL);

  const filteredRecruitment: Recruitment[] = useMemo(() => {
    const allRecruitment: Recruitment[] = recruitment[RECRUITS_TAB.ALL.name];
    const sortedRecruitmentItem: Recruitment[] = allRecruitment.sort((a, b) => {
      return new Date(b.startDateTime).getTime() - new Date(a.startDateTime).getTime();
    });

    if (courseTabStatus.label === COURSE_TAB.ALL.label) {
      return sortedRecruitmentItem;
    }

    return sortedRecruitmentItem.filter((recruitmentItem) =>
      matchProgram(recruitmentItem.title, courseTabStatus.label)
    );
  }, [recruitment, courseTabStatus]);

  return { courseTabStatus, setCourseTabStatus, filteredRecruitment };
};

export default useRecruitList;
