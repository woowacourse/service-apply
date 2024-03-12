import { useContext, createContext } from "react";
import { ERROR_MESSAGE } from "../constants/messages";

export const MemberInfoContext = createContext();

const useMemberInfoContext = () => {
  const memberInfoContext = useContext(MemberInfoContext);

  if (!memberInfoContext) throw Error(ERROR_MESSAGE.HOOKS.CANNOT_FIND_MEMBER_INFO_CONTEXT);

  return memberInfoContext;
};

export default useMemberInfoContext;
