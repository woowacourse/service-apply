export const PATH = {
  HOME: "/",
  RECRUITS: "/recruits",
  SIGN_UP: "/sign-up",
  MY_APPLICATION: "/applications/me",
  ASSIGNMENT: "/assignment/:status",
  APPLICATION_FORM: "/application-forms/:status",
  LOGIN: "/login",
  FIND_PASSWORD: "/find",
  FIND_PASSWORD_RESULT: "/find/result",
  EDIT_PASSWORD: "/edit",
  MY_PAGE: "/mypage",
  EDIT_MY_PAGE: "/mypage/edit",
  WITHDRAWAL_PAGE: "/withdrawal",
} as const;

export const PARAM = {
  APPLICATION_FORM_STATUS: {
    EDIT: "edit",
    NEW: "new",
  },
  ASSIGNMENT_STATUS: {
    EDIT: "edit",
    NEW: "new",
  },
} as const;
