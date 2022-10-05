import { useState, useMemo } from "react";
import { COURSE_TAB } from "../constants/tab";
import useRecruitmentContext from "./useRecruitmentContext";
import { RECRUITS_TAB } from "./../constants/tab";
import { Recruitment } from "../../types/domains/recruitments";

const useRecruitList = () => {
  const [courseTabStatus, setCourseTabStatus] = useState(COURSE_TAB.ALL);
  const { recruitment } = useRecruitmentContext();

  const filteredRecruitment: Recruitment[] | undefined = useMemo(() => {
    if (!recruitment) return recruitment;

    const allRecruitment: Recruitment[] = recruitment[RECRUITS_TAB.ALL.name];
    const sortedRecruitmentItem: Recruitment[] = allRecruitment.sort((a, b) => {
      return new Date(b.startDateTime).getTime() - new Date(a.startDateTime).getTime();
    });

    if (courseTabStatus.label === COURSE_TAB.ALL.label) {
      return sortedRecruitmentItem;
    }

    if (courseTabStatus.label === COURSE_TAB.WOOWA_TECH_COURSE.label) {
      return sortedRecruitmentItem.filter((recruitmentItem) =>
        recruitmentItem.title.includes(courseTabStatus.label)
      );
    }

    return sortedRecruitmentItem.filter((recruitmentItem) => {
      const fullCourseNameArray = recruitmentItem.title.split(" ");
      const courseName = fullCourseNameArray.slice(0, fullCourseNameArray.length - 1);

      return courseName.join(" ").trim() === courseTabStatus.label;
    });
  }, [recruitment, courseTabStatus]);

  return [courseTabStatus, setCourseTabStatus, filteredRecruitment];
};

export default useRecruitList;
