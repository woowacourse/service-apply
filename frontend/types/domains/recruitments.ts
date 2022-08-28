export type RecruitmentStatus = "RECRUITABLE" | "RECRUITING" | "UNRECRUITABLE" | "ENDED";

export type Recruitment = {
  id: number;
  title: string;
  term: {
    id: number;
    name: string;
  };
  recruitable: boolean;
  hidden: boolean;
  startDateTime: string;
  endDateTime: string;
  status: RecruitmentStatus;
};

export type RecruitmentItem = {
  id: number;
  recruitmentId: number;
  title: string;
  poition: number;
  maximumLength: number;
  description: string;
};

export type Mission = {
  id: number;
  title: string;
  description: string;
  submittable: boolean;
  submitted: boolean;
  startDateTime: string;
  endDateTime: string;
  status: RecruitmentStatus;
};

export type Assignment = {
  id: number;
  githubUsername: string;
  pullRequestUrl: string;
  note: string;
};

export type AssignmentData = Omit<Assignment, "id">;
