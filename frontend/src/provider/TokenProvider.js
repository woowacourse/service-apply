import React, { useState } from "react";
import * as Api from "../api";
import { LOCAL_STORAGE_KEY } from "../constants/key";
import { TokenContext } from "../hooks/useTokenContext";

const TokenProvider = ({ children }) => {
  const [token, setToken] = useState(
    () => localStorage.getItem(LOCAL_STORAGE_KEY.ACCESS_TOKEN) || ""
  );

  const postRegister = async (payload) => {
    const { data: token } = await Api.fetchRegister(payload);

    setToken(token);
    localStorage.setItem(LOCAL_STORAGE_KEY.ACCESS_TOKEN, token);
  };

  const resetToken = () => {
    setToken("");
    localStorage.setItem(LOCAL_STORAGE_KEY.ACCESS_TOKEN, "");
  };

  return (
    <TokenContext.Provider value={{ token, postRegister, setToken, resetToken }}>
      {children}
    </TokenContext.Provider>
  );
};

export default TokenProvider;
