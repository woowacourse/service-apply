export const PATH = {
  HOME: "/",
  RECRUITS: "/recruits",
  SIGN_UP: "/sign-up",
  MY_APPLICATION: "/applications/me",
  ASSIGNMENT: "/assignment/:status",
  APPLICATION_FORM: "/application-forms",
  LOGIN: "/login",
  FIND_PASSWORD: "/find",
  FIND_PASSWORD_RESULT: "/find/result",
  EDIT_PASSWORD: "/edit",
  MY_PAGE: "/mypage",
  EDIT_MY_PAGE: "/mypage/edit",
} as const;

export const PARAM = {
  ASSIGNMENT_STATUS: {
    EDIT: "edit",
    NEW: "new",
  },
} as const;
