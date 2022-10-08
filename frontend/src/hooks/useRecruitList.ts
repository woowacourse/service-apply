import { useState, useMemo } from "react";
import useRecruitmentContext from "./useRecruitmentContext";
import { RECRUITS_TAB, PROGRAM_TAB } from "../constants/tab";
import { Recruitment } from "../../types/domains/recruitments";

const matchProgram = (recruitmentTitle: string, programLabel: string) => {
  const programList = Array.from(Object.values(PROGRAM_TAB)).sort(
    (a, b) => b.label.length - a.label.length
  );

  const matchingProgram =
    programList.find(({ label }) => {
      return new RegExp(String.raw`^${label}`, "i").test(recruitmentTitle);
    }) ?? PROGRAM_TAB.ALL;

  return matchingProgram.label === programLabel;
};

const useRecruitList = () => {
  const { recruitment } = useRecruitmentContext();
  const [programTabStatus, setProgramTabStatus] = useState<
    typeof PROGRAM_TAB[keyof typeof PROGRAM_TAB]
  >(PROGRAM_TAB.ALL);

  const filteredRecruitment: Recruitment[] = useMemo(() => {
    const allRecruitment: Recruitment[] = recruitment[RECRUITS_TAB.ALL.name];
    const sortedRecruitmentItem: Recruitment[] = allRecruitment.sort((a, b) => {
      return new Date(b.startDateTime).getTime() - new Date(a.startDateTime).getTime();
    });

    if (programTabStatus.label === PROGRAM_TAB.ALL.label) {
      return sortedRecruitmentItem;
    }

    return sortedRecruitmentItem.filter((recruitmentItem) =>
      matchProgram(recruitmentItem.title, programTabStatus.label)
    );
  }, [recruitment, programTabStatus]);

  return { programTabStatus, setProgramTabStatus, filteredRecruitment };
};

export default useRecruitList;
