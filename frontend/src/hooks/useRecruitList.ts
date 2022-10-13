import { useState, useMemo } from "react";
import useRecruitmentContext from "./useRecruitmentContext";
import { RECRUITS_TAB, PROGRAM_TAB } from "../constants/tab";
import { Recruitment } from "../../types/domains/recruitments";

type ProgramTabStatus = typeof PROGRAM_TAB[keyof typeof PROGRAM_TAB];

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

const sortRecruitmentsByStartDateTime: (recruitments: Recruitment[]) => Recruitment[] = (
  recruitments
) =>
  recruitments.sort((a, b) => {
    return new Date(b.startDateTime).getTime() - new Date(a.startDateTime).getTime();
  });

const filterRecruitmentsByProgramLabel: (
  recruitments: Recruitment[],
  programLabel: ProgramTabStatus["label"]
) => Recruitment[] = (recruitments, programLabel) =>
  recruitments.filter((recruitmentItem) => matchProgram(recruitmentItem.title, programLabel));

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

    if (programTabStatus.label === PROGRAM_TAB.ALL.label) {
      return sortedRecruitments;
    }

    return filterRecruitmentsByProgramLabel(sortedRecruitments, programTabStatus.label);
  }, [recruitment, programTabStatus]);

  return { programTabStatus, setProgramTabStatus, filteredRecruitments };
};

export default useRecruitList;
