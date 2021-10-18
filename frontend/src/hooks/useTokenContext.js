import { useContext, createContext } from "react";
import { ERROR_MESSAGE } from "../constants/messages";

export const TokenContext = createContext();

const useTokenContext = () => {
  const tokenContext = useContext(TokenContext);

  if (!tokenContext) throw Error(ERROR_MESSAGE.HOOKS.CANNOT_FIND_TOKEN_CONTEXT);

  return tokenContext;
};

export default useTokenContext;
