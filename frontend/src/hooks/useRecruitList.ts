import { useState, useMemo } from "react";
import useRecruitmentContext from "./useRecruitmentContext";
import { RECRUITS_TAB, PROGRAM_TAB } from "../constants/tab";
import { Recruitment } from "../../types/domains/recruitments";

type ProgramTabStatus = typeof PROGRAM_TAB[keyof typeof PROGRAM_TAB];

const matchProgram = (recruitmentTitle: string, programName: string) => {
  const programList = Array.from(Object.values(PROGRAM_TAB)).sort(
    (a, b) => b.label.length - a.label.length
  );

  const matchingProgram =
    programList.find(({ korean, english }) => {
      const lowerTitle = recruitmentTitle.toLowerCase();
      return [korean, english].some((it) => lowerTitle.includes(it.toLowerCase()));
    }) ?? PROGRAM_TAB.ALL;

  return matchingProgram.name === programName;
};

const sortRecruitmentsByStartDateTime: (recruitments: Recruitment[]) => Recruitment[] = (
  recruitments
) =>
  recruitments.sort((a, b) => {
    return new Date(b.startDateTime).getTime() - new Date(a.startDateTime).getTime();
  });

const filterRecruitmentsByProgramLabel: (
  recruitments: Recruitment[],
  programName: ProgramTabStatus["name"]
) => Recruitment[] = (recruitments, programName) =>
  recruitments.filter((recruitmentItem) => matchProgram(recruitmentItem.title, programName));

const useRecruitList: () => {
  programTabStatus: ProgramTabStatus;
  setProgramTabStatus: React.Dispatch<React.SetStateAction<ProgramTabStatus>>;
  filteredRecruitments: Recruitment[];
} = () => {
  const { recruitment } = useRecruitmentContext();
  const [programTabStatus, setProgramTabStatus] = useState<
    typeof PROGRAM_TAB[keyof typeof PROGRAM_TAB]
  >(PROGRAM_TAB.ALL);

  const filteredRecruitments: Recruitment[] = useMemo(() => {
    const recruitments: Recruitment[] = recruitment[RECRUITS_TAB.ALL.name];
    const sortedRecruitments: Recruitment[] = sortRecruitmentsByStartDateTime(recruitments);

    if (programTabStatus === PROGRAM_TAB.ALL) {
      return sortedRecruitments;
    }

    return filterRecruitmentsByProgramLabel(sortedRecruitments, programTabStatus.name);
  }, [recruitment, programTabStatus]);

  return { programTabStatus, setProgramTabStatus, filteredRecruitments };
};

export default useRecruitList;
