import { useContext, createContext } from "react";
import { ERROR_MESSAGE } from "../constants/messages";

export const UserInfoContext = createContext();

const useUserInfoContext = () => {
  const userInfoContext = useContext(UserInfoContext);

  if (!userInfoContext)
    throw Error(ERROR_MESSAGE.HOOKS.CANNOT_FIND_USER_INFO_CONTEXT);

  return userInfoContext;
};

export default useUserInfoContext;
