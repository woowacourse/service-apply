const PATH = {
  HOME: "/",
  RECRUITS: "/recruits",
  NEW_APPLICATION: "/applicants/new",
  APPLICATION_FORM: "/application-forms/:status",
  LOGIN: "/login",
  FIND_PASSWORD: "/find",
  FIND_PASSWORD_RESULT: "/find/result",
  EDIT_PASSWORD: "/edit",
};

export const PARAM = {
  APPLICATION_FORM_STATUS: {
    EDIT: "edit",
    NEW: "new",
  },
};

export const createPath = {
  recruits: (status) => `/recruits?status=${status}`,
  recruitsQuery: (status) => `?status=${status}`,
  applicationForm: (status, recruitmentId) =>
    `/application-forms/${status}${
      recruitmentId !== undefined ? `?recruitmentId=${recruitmentId}` : ""
    }`,
  applicationFormQuery: (recruitmentId) => `?recruitmentId=${recruitmentId}`,
};

export default PATH;
