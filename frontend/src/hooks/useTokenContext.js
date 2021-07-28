import { useContext, createContext } from "react";

export const TokenContext = createContext();

const useTokenContext = () => {
  const tokenContext = useContext(TokenContext);

  if (!tokenContext) throw Error("TokenContext가 존재하지 않습니다");

  return tokenContext;
};

export default useTokenContext;
